# The case for dynamic/runtime meta-studies

This document explains the motivation for dynamically combining multiple EDA studies at runtime.

## What are we proposing?

We propose allowing two or more EDA studies to be combined into a single “meta-study” that behaves like an ordinary EDA study for filtering, data retrieval, and visualization.

The combination would happen dynamically on the back end. The client would provide a simple meta-study specification, consisting primarily of a list of study IDs.

There are two types of meta-study: *union* and *extension*.

### Union meta-study

Two or more studies with the same entity structure and the same variables are concatenated.

For example, suppose two studies contain customer records with the following structure:

`customer -> purchase`

and both define the same variables:

* customer hair colour
* customer shoe size
* purchase date
* purchase value

The union meta-study would expose the rows from both studies as though they belonged to one larger study.

This should not be confused with an existing pre-combined study that is loaded into the database and has its own presenter configuration. A runtime union would not itself be stored as a separate study.

### Extension meta-study

An extension meta-study adds new entities from study B to a base study A.

This is possible when all the ancestor entities required by the new entities are already represented in study A.

For example, study A might have the entity structure:

`customer -> purchase`

Study B might contain:

`customer -> purchase -> delivery`

Study B could extend study A with the `delivery` entity, provided that its `customer` and `purchase` tables contain only the entity identifiers and parent identifiers needed to attach each delivery to the corresponding purchase in study A.

The matching ancestor tables in study B should not contain variables of their own. They should contain only identifiers such as:

* `Customer001`
* `Purchase001`
* `Delivery001`

## Where could this be used?

Runtime meta-studies could be presented through the existing EDA user interface and related applications.

Some presenter-derived information may not be available unless presenter metadata can also be merged.

In many cases, an equivalent result could be achieved by deleting an existing study and loading a larger replacement. However, repeatedly rebuilding large studies may not be operationally efficient, particularly when new data arrives incrementally.

### Union

A common use case is user-uploaded longitudinal data arriving in separate batches.

For example:

* February data is uploaded as one user dataset.
* March data is uploaded as another.
* April data is uploaded as a third.

A union meta-study would expose all three datasets together.

Runtime performance would probably not be as good as for a single pre-combined study, but it should remain acceptable.

An alternative would be to implement an “append” mode for user datasets. In append mode, newly uploaded data could either be loaded directly into an existing study or trigger a delete-and-reload operation behind the scenes.

### Extension

Suppose a core study contains customer information:

| Customer ID | Hair colour | Shoe size |
| ----------- | ----------- | --------- |
| Customer001 | Brown       | 9         |
| Customer002 | Black       | 7         |
| Customer003 | Blonde      | 6         |

Its entity structure is:

`customer`

A second study contains purchase data:

| Customer ID | Purchase ID | Item category | Purchase value |
| ----------- | ----------- | ------------- | -------------- |
| Customer001 | Purchase001 | Clothing      | 45.00          |
| Customer001 | Purchase002 | Books         | 18.50          |
| Customer002 | Purchase003 | Shoes         | 72.00          |

Its entity structure is:

`customer -> purchase`

The `customer` table in the second study would contain only customer IDs. It would not duplicate variables such as hair colour or shoe size.

By extending the customer study with the purchase study, visualizations of purchases could use variables from both entity levels.

For example, users could:

* filter purchases by customer shoe size
* stratify purchase values by hair colour
* compare item categories across customer attributes

The same approach could attach many different datasets to a stable set of core entities without requiring the core study to be rebuilt whenever new data is uploaded.

## Implementation overview

More implementation detail is available in *Runtime Meta-Studies in EDA*. The following sections summarize the main approach.

### Client-side

Where the client currently sends a single `studyId`, it would send a list of study IDs for a meta-study:

```json
{
  "studyId": ["customer_data_2025", "customer_data_2026"],
  "outputEntityId": "Customer001",
  "xAxisVariable": {}
}
```

The client would need to know which studies are compatible, although study-level metadata could help determine this.

For union meta-studies, the metadata could include a digest of the entity and variable structure. Studies with matching digests could be considered union-compatible.

For extension meta-studies, compatibility metadata might be more declarative. For example, studies could advertise:

* the schemas they implement
* the entities they define
* the entities they can extend
* the ancestor identifiers they require
* the variables they contribute

At upload time, the user could select a `datasetSchema` from a list of supported data structures.

### Server-side

The general server-side approach would be:

* Decide whether each pair of studies should be unioned or extended.
* Merge their metadata into a combined runtime representation.
* Use that merged metadata to route entity requests to the correct underlying study.

This decision could be implemented in a [new constructor for `ReferenceMetadata`](https://github.com/VEuPathDB/service-eda/blob/a1f14f9a9930ed8f809de0e573a3e3a35c3197bb/src/main/java/org/veupathdb/service/eda/common/model/ReferenceMetadata.java#L33-L41) that accepts a list of `studyDetails`.

The merge rules would be:

* If the entity structures and variables are identical, union the studies.
* If the entity structures match up to a divergence point, and study B has no variables in the matching ancestor entities, extend study A with the new entities from study B.
* Otherwise, reject the combination as incompatible.

When extending study A with study B, referential compatibility between ancestor entity IDs would be assumed rather than fully validated at runtime, because validating every identifier could be prohibitively expensive.

The server-side [study metadata](https://github.com/VEuPathDB/service-eda/blob/master/src/main/java/org/veupathdb/service/eda/common/model/ReferenceMetadata.java#L33) contains a tree of `EntityDef` objects. Each `EntityDef` would need a new `studyId` attribute.

The behaviour would be:

* When creating metadata for study A, each `EntityDef.studyId` refers to `studyA`.
* When extending study A with a new entity X from study B, entity X is added to the tree and its `studyId` refers to `studyB`.
* When unioning study B with study A, `studyB` is added to the `studyId` values for each shared entity.
* Consequently, `EntityDef.studyId` would be represented as a list.

When the subsetting service requests a stream for an entity, it would use the entity-specific study IDs stored in the merged metadata.

For a union meta-study, the `readRow` method in [`EntityStream`](https://github.com/VEuPathDB/service-eda/blob/master/src/main/java/org/veupathdb/service/eda/merge/core/stream/EntityStream.java#L104-L106) would need to move from one underlying study stream to the next after the current stream is exhausted.
