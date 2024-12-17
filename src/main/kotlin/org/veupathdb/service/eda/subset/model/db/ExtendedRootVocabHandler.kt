package org.veupathdb.service.eda.subset.model.db

import org.gusdb.fgputil.db.ResultSetColumnInfo
import org.gusdb.fgputil.db.stream.ResultSetInputStream
import org.gusdb.fgputil.db.stream.ResultSetInputStream.ResultSetRowConverter
import org.veupathdb.service.eda.subset.model.Entity
import org.veupathdb.service.eda.subset.model.filter.Filter
import org.veupathdb.service.eda.subset.model.variable.VariableWithValues
import java.io.InputStream
import java.sql.ResultSet
import javax.sql.DataSource

object ExtendedRootVocabHandler : RootVocabHandler() {
  @JvmStatic
  fun queryStudyVocab(
    schema: String,
    dataSource: DataSource,
    studyEntity: Entity,
    vocabularyVariable: VariableWithValues<*>,
    filters: List<Filter>,
  ): InputStream =
    ResultSetInputStream.getResultSetStream(
      getSqlForVocabQuery(
        schema,
        vocabularyVariable.entity,
        studyEntity,
        vocabularyVariable,
        filters.filter { it.filtersOnVariable(vocabularyVariable) }
      ),
      "study-vocab-${studyEntity.id}",
      dataSource,
      0,
      object : ResultSetRowConverter {
        override fun getHeader() = ByteArray(0)

        override fun getRowDelimiter() = System.lineSeparator().toByteArray()

        override fun getRow(rs: ResultSet, columnInfo: ResultSetColumnInfo): ByteArray {
          val pk = rs.getBytes(studyEntity.pkColName)
          val value = vocabularyVariable.type.convertRowValueToStringValue(rs).toByteArray()

          return ByteArray(pk.size + value.size + 1).apply {
            pk.copyInto(this)
            set(pk.size, 9)
            pk.copyInto(value, pk.size + 1)
          }
        }

        override fun getFooter(): ByteArray = ByteArray(0)

      }
    )

  // TODO: make the superclass method protected instead of private
  private fun getSqlForVocabQuery(appDbSchema: String, outputEntity: Entity, studyEntity: Entity, vocabVar: VariableWithValues<*>, filters: List<Filter>): String {
    return RootVocabHandler::class.java.getDeclaredMethod(
      "getSqlForVocabQuery",
      String::class.java,
      Entity::class.java,
      Entity::class.java,
      VariableWithValues::class.java,
      List::class.java,
    )
      .apply { isAccessible = true }
      .invoke(this, appDbSchema, outputEntity, studyEntity, vocabVar, filters) as String
  }
}
