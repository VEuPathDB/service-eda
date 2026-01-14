# Going Tall: Migration Strategy for Gene Expression Data Model

## Executive Summary

This document outlines the strategy for transitioning **gene expression data** from **wide format** (one column per gene) to **tall format** (separate columns for identifier and value). The primary drivers are front-end efficiency and simplified data management: computing metadata for 20,000+ variables is slow and displaying them in the UI is unwieldy.

**Note**: This migration focuses exclusively on **genomics computations** (differential expression with DESeq2/limma, and WGCNA). Microbiome data will remain in wide format for now, as the multiple taxon-level collections require additional design consideration (see [Microbiome: Out of Scope](#microbiome-out-of-scope)).

**Key Architectural Changes**:

1. **Data Structure**: Wide format (100 samples × 20,000 genes = 100 rows) → Tall format (100 samples × 20,000 genes = 2,000,000 rows with 2 variables) _Note, in both cases the low-level data storage in EDA is effectively tall._

2. **Self-Documenting Variable Identifiers**: Variable `stable_id` values will use standardized human-readable identifiers such as `SEQUENCE_READ_COUNT` instead of arbitrary digests (e.g. `VAR_bdc8e679`):
   - This makes variable IDs self-documenting and affects **upstream data processing** (Study Wrangler must assign these stable IDs during dataset loading)
   - Bespoke IDs are preferred over ontology terms for pragmatic reasons: needed terms don't always exist in current ontologies, and we're unlikely to perform ontology reasoning in the near future

3. **Front-End Predicate Simplification**: Replace complex collection metadata checks with simple string matching: `identifierVar.id === 'VEUPATHDB_GENE_ID' && valueVar.id === 'SEQUENCE_READ_COUNT'`

**Impact by Layer**:
- **Data Layer / Study Wrangler**: Assign self-documenting stable IDs to variables during dataset ingestion
- **RAML/API**: Replace `collectionVariable` with `identifierVariable` + `valueVariable`
- **Java (service-eda)**: Add pivoting step (using R via RServe) to convert tall → wide; minimal other changes
- **R (veupathUtils)**: No changes required - continues to receive wide format after Java/R pivot
- **Front-End (web-monorepo)**: Replace collection-based predicates with variable-pair predicates using stable ID matching

**Key Benefits**: 2 variables instead of 20,000 per dataset, self-documenting term IDs, simplified predicates, faster metadata computation, cleaner UI.

---

## Table of Contents

- [Current Data Flow (Wide Format)](#current-data-flow-wide-format)
  - [1. Data Structure](#1-data-structure)
  - [2. Collection Concept](#2-collection-concept)
  - [3. Column Naming Convention](#3-column-naming-convention)
  - [4. How Data Reaches R](#4-how-data-reaches-r)
  - [5. R Processing](#5-r-processing)
  - [6. PointID Generation](#6-pointid-generation)
- [Proposed Data Flow (Tall Format)](#proposed-data-flow-tall-format)
  - [1. New Data Structure](#1-new-data-structure)
  - [2. Variable Collection Concept Evolution](#2-variable-collection-concept-evolution)
  - [3. API/RAML Changes](#3-apiraml-changes)
  - [4. Required Java Changes](#4-required-java-changes)
  - [5. Self-documenting Variable Identifiers](#5-self-documenting-variable-identifiers)
  - [6. R Code Changes](#6-r-code-changes)
  - [7. Front-End Changes](#7-front-end-changes)
    - [7.1 Overview: From Collection-Based to Variable-Pair-Based Predicates](#71-overview-from-collection-based-to-variable-pair-based-predicates)
    - [7.2 Variable Stable IDs](#72-variable-stable-ids)
    - [7.3 Predicate Design](#73-predicate-design)
    - [7.4 Implementation Guidance](#74-implementation-guidance)
    - [7.5 Comparison: Before vs After](#75-comparison-before-vs-after)
    - [7.6 Benefits for Front-End](#76-benefits-for-front-end)
    - [7.7 Critical Files for Implementation](#77-critical-files-for-implementation)
- [Migration Checklist](#migration-checklist)
  - [RAML Schema](#raml-schema)
  - [Java Layer (service-eda)](#java-layer-service-eda)
  - [R Layer (veupathUtils)](#r-layer-veupathutils)
  - [Front-End](#front-end)
  - [Data Layer / Subsetting](#data-layer--subsetting)
- [Benefits](#benefits)
- [Risks & Considerations](#risks--considerations)
- [References](#references)
  - [Key Code Files](#key-code-files)
  - [Data Flow Summary](#data-flow-summary)
- [Microbiome: Out of Scope](#microbiome-out-of-scope)
- [Questions for Consideration](#questions-for-consideration)

---

## Current Data Flow (Wide Format)

### 1. Data Structure
```
Sample as rows, genes as columns:
sampleID | EUPA_0000123.ENSG00000001 | EUPA_0000123.ENSG00000002 | ... | EUPA_0000123.ENSG00020000
---------|---------------------------|---------------------------|-----|--------------------------
sample1  | 42                        | 100                       | ... | 0
sample2  | 38                        | 95                        | ... | 1
```

### 2. Collection Concept
- A **collection** IS the structural set of 20,000 column variables
- Each gene is a distinct `VariableDef` with its own `variableId` (stable_id)
- `util.getCollectionMembers(collectionSpec)` returns 20,000 `VariableSpec` objects
- Collection configuration: `entityId + collectionVariable`

### 3. Column Naming Convention
Column names follow the pattern: `entityId.variableId`
- Example: `EUPA_0000123.ENSG00000001`
- This is generated by `VariableDef.toDotNotation()` (VariableDef.java:170-171)

### 4. How Data Reaches R
**DifferentialExpressionPlugin.java:80-84**:
```java
List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
computeInputVars.addAll(util.getCollectionMembers(collectionSpec));  // 20k gene columns
computeInputVars.addAll(idColumns);
connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));
connection.voidEval("countData <- " + INPUT_DATA);
```

Creates wide format data that R expects.

### 5. R Processing
**method-differentialExpression.R:114-118** (DESeq example):
```r
# Remove ID columns and zero-only columns
cleanedData <- purrr::discard(collection@data[, -..allIdColumns], ...)

# Transpose: genes become ROWS, samples become COLUMNS
counts <- data.table::transpose(cleanedData)
rownames(counts) <- names(cleanedData)  # Column names → gene identifiers
colnames(counts) <- collection@data[[recordIdColumn]]  # Sample IDs
```

### 6. PointID Generation
The `pointID` in differential expression results comes from:
```
Collection → member variables → VariableDef objects →
entityId.variableId → wide column names → transpose →
rownames(counts) → pointID in output
```

**Critical**: PointIDs are currently `entityId.variableId` dot-notation strings.

---

## Proposed Data Flow (Tall Format)

### 1. New Data Structure
```
Samples as rows, normalized gene-expression observations:
sampleID | EUPA_0000123.VEUPATHDB_GENE_ID | EUPA_0000123.SEQUENCE_READ_COUNT
---------|------------------------|---------------------------
sample1  | ENSG00000001           | 42
sample1  | ENSG00000002           | 100
...
sample1  | ENSG00020000           | 0
sample2  | ENSG00000001           | 38
...
```

The gene column heading follows the standard `entityId.variableId` dot-notation (`EUPA_0000123.VEUPATHDB_GENE_ID`), where `VEUPATHDB_GENE_ID` is our self-documenting variable stable ID. Same goes for the count column.
The gene column **values** are the actual gene identifiers (e.g., `ENSG00000001`, `ENSG00000002`, etc.).

For 100 samples × 20,000 genes = 2,000,000 rows (vs. 100 rows × 20,000 columns)

### 2. Variable Collection Concept Evolution

**Important distinction**: EDA "variable collections" (organizational groupings of variables) are separate from R's `CountDataCollection` class (a data structure). These are independent concepts that happen to share the word "collection."

**EDA variable collections after going tall**:
- Will no longer be used to organize gene expression variables (since there are only 2 variables: gene ID and count/expression)
- May still be used for organizing sample metadata or other variable types
- Gene-level filtering/subsetting is handled externally (already implemented)

**Data model changes**:
- Only 2 actual variables for expression data:
  - Gene column (categorical/string, named `EUPA_0000123.VEUPATHDB_GENE_ID`): contains actual gene IDs (e.g., `ENSG00000001`, `ENSG00000002`)
  - Count/expression column (numeric, named `EUPA_0000123.SEQUENCE_READ_COUNT` for counts or `EUPA_0000123.NORMALIZED_EXPRESSION` for normalized expression): the expression value

**Note**: The R code will still instantiate `CountDataCollection` objects, but these are simply data structures for holding wide-format count matrices. They have nothing to do with EDA's variable collection concept.

### 3. API/RAML Changes
**Current**: `entityId + collectionVariable`
**Proposed**: `entityId + identifierVariable + valueVariable`

Where:
- `identifierVariable` is the gene identifier column (variable with ID `VEUPATHDB_GENE_ID`)
- `valueVariable` is the numeric expression/count column (e.g., variable with ID `SEQUENCE_READ_COUNT` for counts or `NORMALIZED_EXPRESSION` for normalized expression)

The RAML schema will need to be updated to:
- Remove references to `collectionVariable` in differential expression endpoints
- Add `identifierVariable` parameter (the gene identifier column)
- Add `valueVariable` parameter (the count/expression column)
- Document that EDA variable collections are no longer directly referenced in the API for gene expression data (filtering/subsetting happens before the plugin)

### 4. Required Java Changes

**Location**: `DifferentialExpressionPlugin.java:80-84`

**Current**:
```java
computeInputVars.addAll(util.getCollectionMembers(collectionSpec));  // 20k columns
connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));
```

**Proposed** (conceptual):
```java
// Read tall format data stream (already filtered/subsetted to relevant genes)
connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, tallFormatVars));
// tallFormatVars includes: sampleID, gene column (e.g., EUPA_0000123.VEUPATHDB_GENE_ID),
//                          count/expression, ancestorIDs

// Pivot from tall to wide format for R consumption
// The gene column values (ENSG00000001, ENSG00000002, etc.) become the new column names
String geneColName = toColNameOrEmpty(geneVariableSpec);  // e.g., "EUPA_0000123.VEUPATHDB_GENE_ID"
connection.voidEval("countData <- dcast(" + INPUT_DATA +
                    ", " + sampleIDcol + " ~ `" + geneColName + "`" +
                    ", value.var = 'count')");
// Now countData has columns: sampleID, ENSG00000001, ENSG00000002, ..., ENSG00020000
// This is the wide format that CountDataCollection expects
```

This pivoting step is **the key change**. After pivoting, the gene IDs (from the values in the tall gene column) become column names in the wide format. The rest of the Java code proceeds unchanged.

### 5. Self-documenting Variable Identifiers

**Key Design Decision**: Variable `stable_id` values will be self-documenting constants instead of arbitrary digest strings. This makes the system self-documenting and enables front-end predicates to identify compatible data through simple string matching.

**Gene Expression Variable Stable IDs**:
- Gene identifier variable: **`VEUPATHDB_GENE_ID`**
- Count data variable (for DESeq2): **`SEQUENCE_READ_COUNT`**
- Normalized expression variable (for limma): **`NORMALIZED_EXPRESSION`**

**Column Naming in Tall Format**:

The gene column in tall format is named using standard dot-notation: **`EUPA_0000123.VEUPATHDB_GENE_ID`**
The count/expression column is named: **`EUPA_0000123.SEQUENCE_READ_COUNT`** (for counts) or **`EUPA_0000123.NORMALIZED_EXPRESSION`** (for normalized expression)

The **values** in the identifier column are the actual gene IDs: **`ENSG00000001`**, **`ENSG00000002`**, etc.

**After Pivoting to Wide Format**:

After the Java layer pivots from tall to wide, the gene IDs become column names:
```
sampleID | ENSG00000001 | ENSG00000002 | ... | ENSG00020000
---------|--------------|--------------|-----|-------------
sample1  | 42           | 100          | ... | 0
```

**PointID Derivation** remains the same conceptually:
```
Tall format: identifier column values (ENSG00000001, ...) →
Pivot wide: gene IDs become column names →
Transpose: column names become rownames →
DESeq/limma output: rownames become pointID
```

**Output pointIDs will be the actual gene IDs** (e.g., `ENSG00000001`), no longer `entityId.variableId` dot-notation like `EUPA_0000123.ENSG00000001`.

### 6. R Code Changes
**Minimal to none**. The R `veupathUtils` package expects:
- `CountDataCollection` or `ArrayDataCollection` with samples as rows, genes as columns
- Wide format data

The validity checks in `class-CountDataCollection.R:9-30` verify:
- All non-ID columns are numeric ✓ (still true after pivot)
- Count data is integers ✓ (still true after pivot)
- No negative values for counts ✓ (still true after pivot)

The existing transpose and DESeq2/limma processing (method-differentialExpression.R:116-158, 189-241) work as-is.

### 7. Front-End Changes

The "going tall" migration fundamentally changes how the front-end identifies suitable data for computations. Instead of filtering 20,000+ gene variable collections based on metadata annotations, the system will match standardised self-documenting IDs for variable pairs.

#### 7.1 Overview: From Collection-Based to Variable-Pair-Based Predicates

**Current (Wide) Approach:**
```typescript
// Predicate filters collections containing 20,000 gene variables based on metadata
function isCompatibleCollection(collection: CollectionVariableTreeNode): boolean {
  // Complex metadata checks to determine if collection is suitable
  return collection.member === 'gene' &&
         collection.normalizationMethod !== undefined;
}
```

**New (Tall) Approach:**
```typescript
// Predicate matches exact stable IDs for variable pairs
function isDESeqCompatible(identifierVar: Variable, valueVar: Variable): boolean {
  return identifierVar.id === 'VEUPATHDB_GENE_ID' &&  // gene identifier
         valueVar.id === 'SEQUENCE_READ_COUNT';      // count data
}
```

This shift eliminates the need for complex metadata logic and makes predicates self-documenting through standardized stable IDs.

#### 7.2 Variable Stable IDs

**Gene Expression Data**:

| Variable Type | Analysis Method | Stable ID |
|--------------|--------------|---------------|
| Gene identifier | All | **VEUPATHDB_GENE_ID** |
| Integer count | DESeq2 | **SEQUENCE_READ_COUNT** |
| Non-integer expression (array intensities, normalized values) | limma | **NORMALIZED_EXPRESSION** |

#### 7.3 Predicate Design

Predicates will be defined as simple functions matching stable IDs.

Implementation note: variable stable IDs should be defined as constants in a dedicated `*.ts` file, rather than hardcoded as in the examples below.

**Location**: `/home/maccallr/work/EDA/web-monorepo/packages/libs/eda/src/lib/core/components/computations/Utils.ts`

**Gene Expression Predicates:**

```typescript
/**
 * Returns true if the variable pair is compatible with DESeq2 differential expression.
 * Requires: gene identifier variable (VEUPATHDB_GENE_ID) + count data variable (SEQUENCE_READ_COUNT)
 */
export function isDESeqCompatibleVariablePair(
  identifierVariable: Variable,
  valueVariable: Variable
): boolean {
  return (
    identifierVariable.id === 'VEUPATHDB_GENE_ID' &&  // gene identifier
    valueVariable.id === 'SEQUENCE_READ_COUNT'       // count data
  );
}

/**
 * Returns true if the variable pair is compatible with limma differential expression.
 * Requires: gene identifier variable (VEUPATHDB_GENE_ID) + normalized expression variable (NORMALIZED_EXPRESSION)
 */
export function isLimmaCompatibleVariablePair(
  identifierVariable: Variable,
  valueVariable: Variable
): boolean {
  return (
    identifierVariable.id === 'VEUPATHDB_GENE_ID' &&  // gene identifier
    valueVariable.id === 'NORMALIZED_EXPRESSION'       // normalized data
  );
}

/**
 * Returns true if the variable pair is compatible with any differential expression method.
 */
export function isDifferentialExpressionCompatibleVariablePair(
  identifierVariable: Variable,
  valueVariable: Variable
): boolean {
  return (
    isDESeqCompatibleVariablePair(identifierVariable, valueVariable) ||
    isLimmaCompatibleVariablePair(identifierVariable, valueVariable)
  );
}
```

**Helper Functions:**

```typescript
/**
 * Finds a variable by its stable ID within an entity.
 * Example: findVariableById(entity, 'VEUPATHDB_GENE_ID') finds the gene identifier variable.
 */
export function findVariableById(
  entity: StudyEntity,
  variableId: string
): Variable | undefined {
  return entity.variables.find(v => v.id === variableId);
}

/**
 * Returns all variable pairs within an entity that match a predicate.
 * Used for populating dropdowns with compatible variable pairs.
 */
export function findCompatibleVariablePairs(
  entity: StudyEntity,
  predicate: (id: Variable, val: Variable) => boolean
): Array<{ identifierVariable: Variable; valueVariable: Variable }> {
  const pairs: Array<{ identifierVariable: Variable; valueVariable: Variable }> = [];

  for (const identifierVar of entity.variables) {
    for (const valueVar of entity.variables) {
      if (predicate(identifierVar, valueVar)) {
        pairs.push({ identifierVariable: identifierVar, valueVariable: valueVar });
      }
    }
  }

  return pairs;
}
```

#### 7.4 Implementation Guidance

**Configuration Type Changes:**

Update computation configuration types to reference variable pairs instead of collections.

Current (differentialExpression.tsx):
```typescript
export const DifferentialExpressionConfig = t.partial({
  collectionVariable: VariableCollectionDescriptor,  // OLD
  comparator: Comparator,
  differentialExpressionMethod: t.string,
  pValueFloor: t.string,
});
```

New:
```typescript
export const DifferentialExpressionConfig = t.partial({
  identifierVariable: VariableDescriptor,  // NEW: gene identifier (VEUPATHDB_GENE_ID)
  valueVariable: VariableDescriptor,        // NEW: count/expression value
  comparator: Comparator,
  differentialExpressionMethod: t.string,
  pValueFloor: t.string,
});
```

**UI Component Updates:**

Replace `VariableCollectionSingleSelect` with variable pair selection components. Use existing `VariableTreeDropdown` or create a new `VariablePairSelect` component.

**Predicate Integration:**

Update `isEnabledInPicker` functions in all computation plugins:

```typescript
function isEnabledInPicker(params: IsEnabledInPickerParams): boolean {
  const { studyMetadata } = params;
  const entities = entityTreeToArray(studyMetadata.rootEntity);

  // Check if any entity has compatible variable pairs
  return entities.some(entity =>
    findCompatibleVariablePairs(entity, isDESeqCompatibleVariablePair).length > 0
  );
}
```

**Display Updates:**

The most important display consideration is **entity naming**, which will change when we go tall. Since collections are being replaced by variable pairs, entity display names should reflect the data type rather than the collection concept. Typically, plurals are used for user-facing displays.

**Gene Expression Display Examples:**

| Context | Current (Wide) | Proposed (Tall) |
|---------|----------------|-----------------|
| RNA-Seq Counts | "Samples > RNA-Seq sense assays (counts)" | "Samples > RNA-Seq sense raw counts (sample × gene)" |
| RNA-Seq Normalized | "Samples > RNA-Seq sense assays (normalized)" | "Samples > RNA-Seq sense normalized expression (sample × gene)" |
| Antibody Array | "Samples > Antibody array assays" | "Samples > Antibody array expression values (sample × protein)" |

**Naming Rationale:**
- The **(sample × identifier)** suffix clarifies the tall format data shape for users
- Avoids the word "assays" which implied a collection structure
- Emphasizes the data type (counts, expression) rather than the collection mechanism

#### 7.5 Comparison: Before vs After

| Aspect | Before (Collection-Based) | After (Variable-Pair-Based) |
|--------|---------------------------|----------------------------|
| **Data Structure** | Collection with 20,000 gene variables | 2 variables: identifier (VEUPATHDB_GENE_ID) + value (SEQUENCE_READ_COUNT or NORMALIZED_EXPRESSION) |
| **Predicate Logic** | Complex metadata checks: `member='gene' && normalizationMethod !== undefined` | Simple ID matching: `identifierVar.id === 'VEUPATHDB_GENE_ID' && valueVar.id === 'SEQUENCE_READ_COUNT'` |
| **Variable Selection** | Select collection from dropdown | Select variable pair from dropdown |
| **DESeq2** | Filter collections with `member='gene'` + count metadata | Match `VEUPATHDB_GENE_ID` + `SEQUENCE_READ_COUNT` |
| **limma** | Filter collections with `member='gene'` + normalized metadata | Match `VEUPATHDB_GENE_ID` + `NORMALIZED_EXPRESSION` |
| **Metadata Computation** | 20,000+ variables per collection | 2 variables per dataset |
| **UI Display** | Scroll through 20,000 variables | Select from ~2-4 variable pairs per entity |
| **Variable IDs** | Arbitrary digest (VAR_bdc8e679) | Self-documenting term (VEUPATHDB_GENE_ID, SEQUENCE_READ_COUNT) |

#### 7.6 Benefits for Front-End

1. **Simplified Predicate Logic**: String matching against standardized variable stable IDs instead of complex boolean metadata logic
2. **Self-Documenting Code**: Human-readable variable stable IDs make it immediately clear what data type is expected
3. **Metadata Performance**: Computing metadata for 2 variables instead of 20,000+ dramatically improves load times
4. **UI Simplicity**: Users select from a small number of variable pairs instead of scrolling through massive collections
5. **Type Safety**: Stronger guarantees that correct data types are provided to computations
6. **Future Extensibility**: Easy to add new data types by defining new stable IDs (no need to wait for ontology development)

#### 7.7 Critical Files for Implementation

**Front-end files requiring changes:**
- `web-monorepo/packages/libs/eda/src/lib/core/components/computations/Utils.ts` - Define new predicate functions
- `web-monorepo/packages/libs/eda/src/lib/core/components/computations/plugins/differentialExpression.tsx` - Update config and UI for differential expression
- `web-monorepo/packages/libs/eda/src/lib/core/types/study.ts` - Ensure Variable type supports variable stable IDs
- `web-monorepo/packages/libs/eda/src/lib/core/types/variable.ts` - Define VariablePairDescriptor type

---

## Migration Checklist

### RAML Schema
- [ ] Check that https://github.com/VEuPathDB/service-eda/pull/100 has been merged to `main` before working on the wide-to-tall refactor. This will add `limma` method support for differential expression computations to the service code (RAML and Java).
- [ ] Remove `collectionVariable` from differential expression request schema
- [ ] Add `identifierVariable` (gene identifier column) and `valueVariable` (count/expression column)
- [ ] Update documentation: variable collections no longer used for gene expression data organization

### Java Layer (service-eda)
- [ ] Modify `DifferentialExpressionPlugin.java` to:
  - [ ] Accept `valueVariable` instead of `collectionVariable` in config
  - [ ] Read tall format data stream (gene column + count/expression column)
  - [ ] Pivot from tall to wide using `data.table::dcast()` with proper column quoting
  - [ ] Ensure gene ID **values** (ENSG00000001, etc.) become wide column names after pivot
  - [ ] Maintain ancestorID columns through the pivot
  - [ ] Still instantiate `CountDataCollection` object (R data structure, not related to EDA variable collections)
- [ ] Update sample metadata reading (already uses separate read, should work as-is)
- [ ] Test with real RNA-seq data (remove hack at lines 126-131)

### R Layer (veupathUtils)
- [ ] **No code changes required** - the R code is agnostic to EDA variable collections
- [ ] Verify `CountDataCollection` / `ArrayDataCollection` validation still works with pivoted data
- [ ] Confirm gene IDs (ENSG00000001, etc.) appear correctly in `pointID` field of results
- [ ] The `differentialExpression()`, `deseq()`, `limma()` functions work as-is

### Front-End
- [ ] Update differential expression configuration types (remove `collectionVariable`, add `identifierVariable` and `valueVariable`)
- [ ] Define gene expression variable stable ID constants for easy reference throughout codebase
- [ ] Implement new predicate functions in `Utils.ts`:
  - [ ] `isDESeqCompatibleVariablePair()` - VEUPATHDB_GENE_ID + SEQUENCE_READ_COUNT
  - [ ] `isLimmaCompatibleVariablePair()` - VEUPATHDB_GENE_ID + NORMALIZED_EXPRESSION
  - [ ] `isDifferentialExpressionCompatibleVariablePair()` - combination predicate
  - [ ] Helper functions: `findVariableById()`, `findCompatibleVariablePairs()`
- [ ] Create/update variable pair selection UI components (or adapt `VariableTreeDropdown`)
- [ ] Update `isEnabledInPicker` function in `differentialExpression.tsx` to check for compatible variable pairs
- [ ] Update configuration panel in `differentialExpression.tsx`:
  - [ ] Replace `VariableCollectionSingleSelect` with variable pair selection
  - [ ] Update configuration description to display variable pairs (e.g., "Gene × Count")
- [ ] Handle pointIDs as actual gene IDs like `ENSG00000001` (no longer `entityId.variableId` dot-notation)
- [ ] Verify visualization plugins consume new pointID format correctly
- [ ] Test differential expression computations with new variable pair selection

### Data Layer / Subsetting
- [ ] Gene filtering/subsetting (already covered per notes)

---

## Benefits

1. **Front-end efficiency**: Metadata computation for 2 variables instead of 20,000
2. **UI simplicity**: No need to scroll through 20,000 variables
3. **Storage flexibility**: Sparse data representation possible
4. **Query efficiency**: Easier to filter by gene sets
5. **Minimal R changes**: Well-tested computation code remains stable

---

## Risks & Considerations

1. **Data volume in transit**: 2M rows vs. 100 rows (mitigated by subsetting before Java)
2. **Pivoting overhead**: Additional R computation step in Java layer
3. **Memory usage**: Pivot operation may temporarily require more memory
4. **PointID format change**: Downstream consumers must handle gene IDs instead of dot-notation

---

## References

### Key Code Files
- **Java**: `src/main/java/org/veupathdb/service/eda/compute/plugins/differentialexpression/DifferentialExpressionPlugin.java`
- **R**: `veupathUtils/R/method-differentialExpression.R`
- **R**: `veupathUtils/R/class-CountDataCollection.R`
- **R**: `veupathUtils/R/class-ArrayDataCollection.R`
- **Java Util**: `src/main/java/org/veupathdb/service/eda/common/plugin/util/PluginUtil.java`
- **Model**: `src/main/java/org/veupathdb/service/eda/common/model/VariableDef.java`

### Data Flow Summary
```
[Wide Format]
Tabular data → Java reads 20k columns → R CountDataCollection (wide) →
transpose → DESeq2/limma (genes as rows) → results with pointIDs

[Tall Format - Proposed]
Tabular data (tall) → Java reads 3 columns → Java/R pivot to wide →
R CountDataCollection (wide) → transpose → DESeq2/limma → results with pointIDs
```

---

## Microbiome: Out of Scope

The wide-to-tall migration is currently **scoped exclusively for gene expression computations** (differential expression with DESeq2/limma, and WGCNA). Microbiome data will remain in wide format for the time being.

**Why microbiome is more complex:**

Microbiome data has **multiple variable collections per entity**, one for each taxonomic level (Kingdom, Phylum, Class, Order, Family, Genus, Species). The current UI uses `VariableCollectionSingleSelect` to filter collections from each entity and displays `collection.displayName` in dropdown menus, allowing users to select which taxonomic level they want to analyze.

Going tall for microbiome would require:
1. **Variable stable ID design** that encodes taxonomic level (e.g., `TAXON_COUNTS:Kingdom`, `TAXON_COUNTS:Phylum`, etc.)
2. **UI filtering logic** that can extract taxonomic level information from variable IDs without resorting to string splitting (e.g., avoid `split(':')` approaches)
3. **Display label generation** from structured variable metadata rather than hard-coded string manipulation
4. **Predicate functions** that can match taxonomic levels flexibly

These design considerations need more thought before extending the tall format approach to microbiome computations. The current wide format works well for microbiome use cases, so this is not urgent.

---

## Questions for Consideration

1. Should pointIDs include `entityId.` prefix for consistency (e.g., `EUPA_0000123.ENSG00000001`), or just the gene IDs (e.g., `ENSG00000001`)?
2. How should the pivot handle duplicate sample-gene combinations (e.g., average, sum, first)?
3. What column name convention should be used post-pivot if gene IDs contain special characters?
4. Should we support both wide and tall formats during a transition period?
5. **Variable Stable ID constants structure**: Should we define variable stable IDs as simple string constants or as structured objects with display names?
   - **Option A - Simple strings**:
     ```typescript
     export const VEUPATHDB_GENE_ID = 'VEUPATHDB_GENE_ID';
     export const SEQUENCE_READ_COUNT = 'SEQUENCE_READ_COUNT';
     export const NORMALIZED_EXPRESSION = 'NORMALIZED_EXPRESSION';
     ```
   - **Option B - Structured with display names**:
     ```typescript
     export const VEUPATHDB_GENE_ID = {
       id: 'VEUPATHDB_GENE_ID',
       displayName: 'Gene ID'
     };
     export const SEQUENCE_READ_COUNT = {
       id: 'SEQUENCE_READ_COUNT',
       displayName: 'Sequence Read Count'
     };
     export const NORMALIZED_EXPRESSION = {
       id: 'NORMALIZED_EXPRESSION',
       displayName: 'Normalized Expression'
     };
     ```
   - **Trade-offs**: Option B provides human-readable labels useful for error messages, tooltips, logging, and debugging. However, it requires accessing `.id` everywhere (e.g., `identifierVar.id === VEUPATHDB_GENE_ID.id`). Option A is simpler but loses the self-documenting display names. Could be YAGNI, or could be valuable for UX and developer experience. 

---

**Document Date**: 2026-01-14
**Status**: Planning / Pre-implementation
