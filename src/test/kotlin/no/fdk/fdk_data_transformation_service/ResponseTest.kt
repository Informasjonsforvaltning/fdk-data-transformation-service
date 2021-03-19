package no.fdk.fdk_data_transformation_service

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.nhaarman.mockitokotlin2.*
import no.fdk.fdk_data_transformation_service.utils.ApiTestContext
import no.fdk.fdk_data_transformation_service.utils.LOCAL_SERVER_PORT
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResponseTest: ApiTestContext() {
    private val request: HttpRequest = mock()
    private val response: HttpResponse = mock()

    @BeforeEach
    fun beforeTest() {
        reset(request, response)
    }

    @Test
    fun wrongPath() {
        whenever(request.path).thenReturn("/does-not-exist")
        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(404, firstValue)
        }
    }

    @Test
    fun wrongMethod() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("GET")
        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(405, firstValue)
        }
    }

    @Test
    fun missingCatalogType() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun wrongCatalogType() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("doesnotexist"))))
        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun tooManyCatalogTypes() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("datasets", "concepts"))))

        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun missingEnvironment() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("datasets"))))
        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun wrongEnvironment() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(
            mapOf(
                Pair("catalog", listOf("datasets")),
                Pair("environment", listOf("doesnotexist"))
            )
        )
        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun tooManyEnvironments() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(
            mapOf(
                Pair("catalog", listOf("datasets")),
                Pair("environment", listOf("prod", "demo"))
            )
        )

        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun acceptsDatasets() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(
            mapOf(
                Pair("catalog", listOf("datasets")),
                Pair("environment", listOf("staging"))
            )
        )

        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsDataServices() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(
            mapOf(
                Pair("catalog", listOf("dataservices")),
                Pair("environment", listOf("staging"))
            )
        )

        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsConcepts() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(
            mapOf(
                Pair("catalog", listOf("concepts")),
                Pair("environment", listOf("staging"))
            )
        )

        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsInformationModels() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(
            mapOf(
                Pair("catalog", listOf("informationmodels")),
                Pair("environment", listOf("staging"))
            )
        )

        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsPublicServices() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(
            mapOf(
                Pair("catalog", listOf("publicservices")),
                Pair("environment", listOf("staging"))
            )
        )

        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsEvents() {
        whenever(request.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(
            mapOf(
                Pair("catalog", listOf("events")),
                Pair("environment", listOf("staging"))
            )
        )

        DataTransformationService().service(request, response)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

}
