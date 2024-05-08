package merging

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import compute.ComputeTestCase
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.commons.codec.binary.Hex
import org.apache.logging.log4j.kotlin.logger
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.BufferedWriter
import java.io.File
import java.net.URL
import java.nio.file.Path
import java.security.MessageDigest
import java.util.*
import java.util.stream.Stream
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MergePerfTestRunner {

    private val AuthToken: String = System.getProperty("AUTH_TOKEN")
    private val AuthTokenKey: String = "Auth-Key"
    private val JsonMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
    private var OutputWriter: BufferedWriter? = null

    // Files in the testdata resource directory must follow structure testdata/{dataset-type}/{upload-file}
    private val TestFilesDir = "testdata/merge-requests"

    @BeforeAll
    internal fun setup() {
        RestAssured.baseURI = System.getProperty("BASE_URL")
        RestAssured.port = System.getProperty("SERVICE_PORT").toInt()
        RestAssured.useRelaxedHTTPSValidation()
        logger().info("File: ${System.getProperty("TEST_REPORT_OUTPUT_FILE")}")
        OutputWriter = File(System.getProperty("TEST_REPORT_OUTPUT_FILE")).bufferedWriter()
        OutputWriter?.write("studyId,filterCount,distinctEntityCount,duration,valuesReturned\n")
    }

    @AfterAll
    internal fun teardown() {
        if (OutputWriter != null) {
            OutputWriter!!.close()
        }
        logger().info("Results can be found in ${System.getProperty("TEST_REPORT_OUTPUT_FILE")}")
    }

    @ParameterizedTest
    @MethodSource("mergeRequestProvider")
    fun test(body: JsonNode) {
        val study = body.get("studyId").asText()
        val numFilters = body.get("filters").elements().asSequence().count()
        val distinctOutputEntityCount = body.get("outputVariables").elements().asSequence()
            .distinctBy { varSpec -> varSpec.get("entityId") }
            .count()
        val (numLines, timeTaken) = measureTimedValue {
            RestAssured.given()
                .contentType(ContentType.JSON)
                .header(AuthTokenKey, AuthToken)
                .body(body)
                // Execute request
                .`when`()
                .post("merging/query")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asInputStream()
                .bufferedReader()
                .lines()
                .count()
        }
        val checksum = Hex.encodeHexString(MessageDigest.getInstance("SHA-1").digest(body.toString().toByteArray()))
        OutputWriter?.write("$study,$numFilters,$distinctOutputEntityCount,${timeTaken.inWholeMilliseconds},$numLines,$checksum\n")
        logger().info("$study, $numFilters, $distinctOutputEntityCount, $timeTaken, $numLines, $checksum")
    }

    private fun mergeRequestProvider(): Stream<JsonNode> {
        val loader = Thread.currentThread().contextClassLoader
        val url: URL = loader.getResource(TestFilesDir)!!
        val testDataDir: String = url.path
        return Arrays.stream(Path.of(testDataDir).toFile().listFiles())
            .map { file -> JsonMapper.readTree(file) }
    }
}