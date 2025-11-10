plugins {
  java
  alias(libs.plugins.kotlin)
  alias(libs.plugins.shadowJar)
  alias(libs.plugins.containerUtils)
}

containerBuild {
  // General project level configuration.
  project {
    // Project Name
    name = "eda-service"

    // Project Group
    group = "org.veupathdb.service.eda"

    // Project Version
    version = "3.0.0"

    // Project Root Package
    projectPackage = "org.veupathdb.service.eda"
  }

  // Docker build configuration.
  docker {
    imageName = "eda-service"
  }
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
    vendor = JvmVendorSpec.AMAZON
  }
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(21)
    vendor = JvmVendorSpec.AMAZON
  }

  compilerOptions {
    freeCompilerArgs = listOf("-Xjvm-default=all")
  }
}

tasks.shadowJar {
  exclude("**/Log4j2Plugins.dat")
  archiveFileName.set("service.jar")
}

repositories {
  mavenCentral()
  mavenLocal()
  maven {
    name = "GitHubPackages"
    url  = uri("https://maven.pkg.github.com/veupathdb/maven-packages")
    credentials {
      username = if (extra.has("gpr.user")) extra["gpr.user"] as String? else System.getenv("GITHUB_USERNAME")
      password = if (extra.has("gpr.key")) extra["gpr.key"] as String? else System.getenv("GITHUB_TOKEN")
    }
  }
}

// ensures changing modules are never cached
configurations.all {
  resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

dependencies {
  implementation(libs.vpdb.edaSubsetting)

  implementation(libs.rServe)

  implementation(libs.bundles.fgputil)
  implementation(libs.bundles.platform.http)
  implementation(libs.bundles.platform.async)
  implementation(libs.bundles.serialization)

  implementation(libs.bundles.logging)
  implementation(libs.bundles.metrics)
  implementation(libs.bundles.email)

  implementation(libs.sql.imports)
  implementation(libs.sql.extras)
  implementation(libs.sql.stubDb)

  implementation(libs.util.checkedLambdas)
  implementation(libs.util.idGenerator)

  testImplementation(libs.bundles.test.implementation)
  testRuntimeOnly(libs.bundles.test.runtime)
}

tasks.getting(Test::class) {
  useJUnitPlatform()
}
