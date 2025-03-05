plugins {
  kotlin("jvm") version "2.0.20" // needed for local compute import
  java
  id("org.veupathdb.lib.gradle.container.container-utils") version "5.0.4"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

// configure VEupathDB container plugin
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

//
// Project Dependencies
//

// versions
val coreLib = "9.1.1"            // Container core lib version
val fgputil = "2.16.0-jakarta"   // FgpUtil version

// use local EDA compute compiled schema if project exists, else use released version;
// this mirrors the way we use local EdaCommon code if available
val commonRamlOutFileName = "$projectDir/schema/eda-compute-lib.raml"

// ensures changing modules are never cached
configurations.all {
  resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

dependencies {

  // REngine Java client to RServe
  //implementation("org.rosuda.REngine:REngine:2.1.0")
  implementation("org.rosuda.REngine:Rserve:1.8.1")

  // VEuPathDB libs, prefer local checkouts if available
  implementation(findProject(":core") ?: "org.veupathdb.lib:jaxrs-container-core:${coreLib}")
  implementation(findProject(":libEdaSubsetting") ?: "org.veupathdb.lib:eda-subsetting:6.1.0")

  // published VEuPathDB libs
  implementation("org.gusdb:fgputil-core:${fgputil}")
  implementation("org.gusdb:fgputil-client:${fgputil}")
  implementation("org.gusdb:fgputil-db:${fgputil}")
  implementation("org.veupathdb.lib:compute-platform:1.8.5")
  implementation("org.veupathdb.lib.s3:s34k-minio:0.7.2+s34k-0.11.0")

  // Jersey
  implementation("org.glassfish.jersey.core:jersey-server:3.1.10")

  // Jackson
  implementation("org.veupathdb.lib:jackson-singleton:3.2.1")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")

  // Log4J
  implementation("org.apache.logging.log4j:log4j-api:2.24.3")
  implementation("org.apache.logging.log4j:log4j-core:2.24.3")
  runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.24.3")
  implementation("org.slf4j:slf4j-api:2.0.16")

  // Metrics
  implementation("io.prometheus:simpleclient:0.16.0")
  implementation("io.prometheus:simpleclient_common:0.16.0")

  // Utils
  implementation("io.vulpine.lib:Jackfish:1.1.0")
  implementation("com.devskiller.friendly-id:friendly-id:1.1.0")
  implementation("io.vulpine.lib:sql-import:0.2.1")
  implementation("io.vulpine.lib:lib-query-util:2.1.0")
  implementation("javax.mail", "mail", "1.5.0-b01")
  implementation("org.antlr", "ST4", "4.3.4") // Access service email template parsing

  // Pico CLI
  implementation("info.picocli:picocli:4.7.6")
  annotationProcessor("info.picocli:picocli-codegen:4.7.6")

  // Job IDs
  implementation("org.veupathdb.lib:hash-id:1.1.0")

  // Stub database
  implementation("org.hsqldb:hsqldb:2.7.1")

  // Unit Testing
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0")
  testImplementation("org.mockito:mockito-core:5.15.2")
  testImplementation("org.veupathdb.lib.test", "test-utils", "1.1.2")
  testImplementation("org.awaitility:awaitility:4.2.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.0")
}

val test by tasks.getting(Test::class) {
  // Use junit platform for unit tests
  useJUnitPlatform()
}
