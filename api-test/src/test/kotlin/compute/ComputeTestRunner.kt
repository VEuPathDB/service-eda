package compute

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.logging.log4j.kotlin.logger
import org.awaitility.Awaitility
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.net.URL
import java.nio.file.Path
import java.time.Duration
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComputeTestRunner {
    private val AuthToken: String = System.getProperty("AUTH_TOKEN")
    private val AuthTokenKey: String = "Auth-Key"
    private val YamlMapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule.Builder().build())

    // Files in the testdata resource directory must follow structure testdata/{dataset-type}/{upload-file}
    private val TestFilesDir = "testdata"
    private val TestSuite = loadTestSuite()

    @BeforeAll
    internal fun setup() {
        RestAssured.baseURI = System.getProperty("BASE_URL")
        RestAssured.port = System.getProperty("SERVICE_PORT").toInt()
        RestAssured.useRelaxedHTTPSValidation()
    }

    @ParameterizedTest
    @MethodSource("computeTestCaseProvider")
    fun parameterizedComputeTest(input: ComputeTestCase) {
        logger().info("Compute body: " + input.computeBody)

        // Submit compute
        RestAssured.given()
            .contentType(ContentType.JSON)
            .header(AuthTokenKey, AuthToken)
            .body(input.computeBody)
            // Execute request
            .`when`()
            .post("computes/alphadiv?autostart=true")
            .then()
            .statusCode(200)

        // Wait for completion of compute.
        Awaitility.given()
            .atMost(Duration.ofSeconds(90))
            .pollInterval(Duration.ofSeconds(2))
            .until {
                 val status = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(AuthTokenKey, AuthToken)
                    .body(input.computeBody)
                    // Execute request
                    .`when`()
                    .post("computes/alphadiv?autostart=true")
                    .then()
                    .statusCode(200)
                    .extract()
                    .path<String>("status")
                logger().info("Querying status of compute job. Status: $status")
                status == "complete"
            }
        val body = RestAssured.given()
            .contentType(ContentType.JSON)
            .header(AuthTokenKey, AuthToken)
            .body(input.vizBody)
            // Execute request
            .`when`()
            .post("apps/alphadiv/visualizations/boxplot")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        logger().info("Finished compute test with body: $body")
    }

    /**
     * Provide donut test cases.
     */
    private fun computeTestCaseProvider(): Stream<ComputeTestCase> {
        return TestSuite.computeTests.stream()
            .map {
                ComputeTestCase(
                    computeBody = it.computeBody,
                    vizBody = it.vizBody
                )
            }
    }


    private fun loadTestSuite(): ComputeTestSuite {
        val loader = Thread.currentThread().contextClassLoader
        val url: URL = loader.getResource(TestFilesDir)!!
        val testDataDir: String = url.path
        val testConfig = Path.of(testDataDir, "compute-tests.yaml")
        return YamlMapper.readValue(testConfig.toFile())
    }

}