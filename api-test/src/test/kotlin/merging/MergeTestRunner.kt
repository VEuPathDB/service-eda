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
class MergeTestRunner {

    private val AuthToken: String = System.getProperty("AUTH_TOKEN")
    private val AuthTokenKey: String = "Auth-Key"


    @BeforeAll
    internal fun setup() {
        RestAssured.baseURI = System.getProperty("BASE_URL")
        RestAssured.port = System.getProperty("SERVICE_PORT").toInt()
        RestAssured.useRelaxedHTTPSValidation()
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

        lines.skip(1).forEach {
            val values = it.split("\t")
            val actual = values[0]
            val expected = "[${values[1]},${values[2]},${values[3]}]"
            Assertions.assertEquals(expected, actual)
        }
    }
}