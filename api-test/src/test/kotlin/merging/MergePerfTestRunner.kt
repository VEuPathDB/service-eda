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
import org.junit.jupiter.api.*
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
@Tag("Performance")
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
    fun createMapping(body: JsonNode) {
        val checksum = Hex.encodeHexString(MessageDigest.getInstance("SHA-1").digest(body.toString().toByteArray()))
        println("$checksum:$body")
    }

    @Test
    fun testConcatDerivedVar() {
        val body = """
            {
                "studyId": "WASHBBANG-1",
                "entityId": "EUPATH_0000096",
                "outputVariables":[
                  { "entityId": "EUPATH_0000096", "variableId": "testConcat" },
                  { "entityId": "EUPATH_0000096", "variableId": "EUPATH_0035110"},
                  { "entityId": "PCO_0000024", "variableId": "ENVO_01000614"},
                  { "entityId": "EUPATH_0035127", "variableId": "EUPATH_0035036"}
                ],
                "filters": [],
                "derivedVariables": [{
                    "entityId": "EUPATH_0000096",
                    "variableId": "testConcat",
                    "displayName": "test concat",
                    "functionName": "concatenation",
                    "config": {
                      "prefix": "['",
                      "delimiter": "','",
                      "suffix": "']",
                      "inputVariables": [
                        { "entityId": "EUPATH_0000096", "variableId": "EUPATH_0035110"},
                        { "entityId": "PCO_0000024", "variableId": "ENVO_01000614"},
                        { "entityId": "EUPATH_0035127", "variableId": "EUPATH_0035036"}
                      ]
                    }
                }]
            }
        """.trimIndent()
        val lines: Stream<String> = RestAssured.given()
            .contentType(ContentType.JSON)
            .header(AuthTokenKey, AuthToken)
            .body(body)
            .`when`()
            .post("merging/query")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asInputStream()
            .bufferedReader()
            .lines()
        lines.forEach {
            val values = it.split("\t")
            val actual = values[0]
            val expected = "[${values[1]},${values[2]},${values[3]}]"
            Assertions.assertEquals(expected, actual)
        }
    }

    @ParameterizedTest
    @MethodSource("mergeRequestProvider")
    fun test(body: JsonNode) {
        val study = body.get("studyId").asText()
        val numFilters = body.get("filters").elements().asSequence().count()
        val filters = body.get("filters").elements().asSequence()
            .map { filter -> filter.get("type").textValue() }
            .joinToString("|")

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
        OutputWriter?.write("$study,$numFilters,$distinctOutputEntityCount,${timeTaken.inWholeMilliseconds},$numLines,$checksum,$filters\n")
        logger().info("$study, $numFilters, $distinctOutputEntityCount, $timeTaken, $numLines, $checksum, $filters")
    }

    private fun mergeRequestProvider(): Stream<JsonNode> {
        val loader = Thread.currentThread().contextClassLoader
        val url: URL = loader.getResource(TestFilesDir)!!
        val testDataDir: String = url.path
        return Arrays.stream(Path.of(testDataDir).toFile().listFiles())
            .flatMap { file -> Stream.generate { file }.limit(3) } // Use each one 5 times.
            .map { file -> JsonMapper.readTree(file) }
    }
}