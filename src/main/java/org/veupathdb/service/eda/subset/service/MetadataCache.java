package org.veupathdb.service.eda.subset.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.subset.model.Study;
import org.veupathdb.service.eda.subset.model.StudyOverview;
import org.veupathdb.service.eda.subset.model.db.StudyFactory;
import org.veupathdb.service.eda.subset.model.db.StudyProvider;
import org.veupathdb.service.eda.subset.model.db.VariableFactory;
import org.veupathdb.service.eda.subset.model.reducer.MetadataFileBinaryProvider;
import org.veupathdb.service.eda.subset.model.variable.binary.BinaryFilesManager;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MetadataCache implements StudyProvider {
  private static final Logger LOG = LogManager.getLogger(MetadataCache.class);
  public static final boolean SORT_ENTITIES_ENABLED = true;

  // instance fields
  private List<StudyOverview> _studyOverviews;  // cache the overviews
  private final BinaryFilesManager _binaryFilesManager;
  private final Supplier<StudyProvider> _sourceStudyProvider;
  private final Map<String, Study> _studies = new HashMap<>(); // cache the studies
  private final Map<String, Boolean> _studyHasFilesCache = new HashMap<>();
  private final ScheduledExecutorService _scheduledThreadPool = Executors.newScheduledThreadPool(1); // Shut this down.
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private final Optional<CountDownLatch> _appDbReadySignal;

  public MetadataCache(BinaryFilesManager binaryFilesManager, CountDownLatch appDbReadySignal) {
    _binaryFilesManager = binaryFilesManager;
    _sourceStudyProvider = this::getCuratedStudyFactory; // Lazily initialize to ensure database connection is established before construction.
    _scheduledThreadPool.scheduleAtFixedRate(this::synchronizeCacheState, 0L, 5L, TimeUnit.MINUTES);
    _appDbReadySignal = Optional.of(appDbReadySignal);
  }

  // Visible for testing
  MetadataCache(BinaryFilesManager binaryFilesManager,
                StudyProvider sourceStudyProvider,
                Duration refreshInterval) {
    _binaryFilesManager = binaryFilesManager;
    _sourceStudyProvider = () -> sourceStudyProvider;
    _scheduledThreadPool.scheduleAtFixedRate(this::synchronizeCacheState, 0L,
      refreshInterval.toMillis(), TimeUnit.MILLISECONDS);
    _appDbReadySignal = Optional.empty();
  }

  @Override
  public synchronized Study getStudyById(String studyId) {
    return _studies.computeIfAbsent(studyId, id -> getCuratedStudyFactory().getStudyById(id));
  }

  @Override
  public synchronized List<StudyOverview> getStudyOverviews() {
    if (_studyOverviews == null) {
      _studyOverviews = _sourceStudyProvider.get().getStudyOverviews();
    }
    return Collections.unmodifiableList(_studyOverviews);
  }

  public synchronized boolean studyHasFiles(String studyAbbrev) {
    _studyHasFilesCache.computeIfAbsent(studyAbbrev, _binaryFilesManager::studyHasFiles);
    return _studyHasFilesCache.get(studyAbbrev);
  }

  private StudyProvider getCuratedStudyFactory() {
    return new StudyFactory(
      Resources.getApplicationDatabase().getDataSource(),
      Resources.getAppDbSchema(),
      StudyOverview.StudySourceType.CURATED,
      new VariableFactory(
        Resources.getApplicationDatabase().getDataSource(),
        Resources.getAppDbSchema(),
        new MetadataFileBinaryProvider(_binaryFilesManager),
        this::studyHasFiles),
      SORT_ENTITIES_ENABLED
    );
  }

  public synchronized void clear() {
    _studyOverviews = null;
    _studies.clear();
    _studyHasFilesCache.clear();
  }

  public void shutdown() {
    _scheduledThreadPool.shutdown();
  }

  private void synchronizeCacheState() {
    LOG.info("Synchronizing cache state");
    // Wait until main thread signals that the application database is ready. If we try to access it before, it will
    // not throw an exception, but it will use the internal stub DB implementation which will result in missing studies.
    if (_appDbReadySignal.isPresent()) {
      LOG.debug("awaiting application database to be ready");
      try {
        _appDbReadySignal.get().await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        LOG.warn("Thread interrupted while awaiting Application database readiness.", e);
        return;
      }
    }

    LOG.debug("checking which studies are out of date in cache.");
    List<StudyOverview> dbStudies = _sourceStudyProvider.get().getStudyOverviews();
    List<Study> studiesToRemove = _studies.values().stream()
      .filter(study -> isOutOfDate(study, dbStudies))
      .toList();

    synchronized (this) {
      LOG.debug("Removing the following out of date or missing studies from cache: {}",
        () -> studiesToRemove.stream().map(StudyOverview::getStudyId).collect(Collectors.joining(",")));

      dbStudies.forEach(study -> {
        boolean studyHasFiles = _binaryFilesManager.studyHasFiles(study.getStudyId());

        // Check if the files exist and cache the result. Get previous value from cache at the same time.
        boolean usedToHaveFiles = Boolean.TRUE.equals(_studyHasFilesCache.put(study.getStudyId(), studyHasFiles));

        if (!usedToHaveFiles && studyHasFiles) {
          // Remove study if files have been added since last time cache was populated.
          // Otherwise, the cache will reflect that the study has files, but necessary metadata will be missing from cache.
          _studies.remove(study.getStudyId());
        }
      });

      // Replace study overviews with those available in DB.
      _studyOverviews = dbStudies;

      // Remove any studies with full metadata loaded if they have been modified.
      // They will be lazily repopulated when requested by users.
      _studies.entrySet().removeIf(study ->
        studiesToRemove.stream().anyMatch(removeStudy -> removeStudy.getStudyId().equals(study.getKey())));
    }
  }

  private boolean isOutOfDate(StudyOverview studyOverview, List<StudyOverview> dbStudies) {
    Optional<StudyOverview> matchingDbStudy = dbStudies.stream()
      .filter(dbStudy -> dbStudy.getStudyId().equals(studyOverview.getStudyId()))
      .findAny();

    // If in DB, check if it's out of date.
    return matchingDbStudy.map(overview -> overview.getLastModified().after(studyOverview.getLastModified()))
      // Study not in DB anymore, remove it from cache.
      .orElse(true);
  }
}

