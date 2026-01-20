# EDA Data Service

A microservice providing Exploratory Data Analysis capabilities and data access to EDA client applications within the VEuPathDB ecosystem.

## Quick Reference

- **Language:** Java 21 (primary) + Kotlin
- **Build:** Gradle 8.7
- **API:** JAX-RS with Jersey, defined in `api.raml`
- **Database:** PostgreSQL
- **Async:** RabbitMQ for job queues, S3/MinIO for storage

## Project Structure

| Directory | Purpose |
|-----------|---------|
| `src/main/java/org/veupathdb/service/eda/` | Main Java source |
| `src/main/kotlin/org/veupathdb/service/eda/` | Kotlin utilities |
| `schema/` | RAML API type definitions |
| `gradle/libs.versions.toml` | Dependency versions |

## Main Modules

- **subset/** - Data extraction and filtering, study/entity metadata
- **merge/** - Combining entities, derived variable transforms
- **compute/** - Statistical analysis job processing (RabbitMQ + S3)
- **data/** - Visualization and analytics plugins
- **access/** - Dataset permission management
- **user/** - User profiles, analysis import/export
- **download/** - File streaming and export

## Build Commands

```bash
./gradlew build        # Build the project
./gradlew test         # Run tests
./gradlew shadowJar    # Create executable jar
make docker            # Build Docker image
```

## Key Files

- `Main.java` - Application entry point
- `Resources.java` - JAX-RS resource registration
- `api.raml` - REST API specification
- `EnvConfig.java` - Environment configuration

## Architecture Notes

- Plugin architecture for transforms (BMI, unit conversion, etc.) and reductions (Mean, Sum)
- MetadataCache for in-memory study/entity caching
- BinaryValuesStreamer for efficient large dataset streaming
- @Authenticated annotation for permission checking
- Async compute jobs via RabbitMQ with results in S3
