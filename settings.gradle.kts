plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

// adds repos for gradle plugin resolution and ensures github creds are provided to the build
apply {
  from("https://raw.githubusercontent.com/VEuPathDB/lib-gradle-container-utils/v5.0.5/includes/common.settings.gradle.kts")
}
