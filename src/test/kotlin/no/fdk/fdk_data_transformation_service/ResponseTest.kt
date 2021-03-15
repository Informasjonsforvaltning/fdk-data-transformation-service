package no.fdk.fdk_data_transformation_service

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResponseTest {

    @Mock
    private val request: HttpRequest? = null

    @Mock
    private val response: HttpResponse? = null

    @BeforeEach
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun wrongPath() {
        whenever(request!!.path).thenReturn("/does-not-exist")
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(404, firstValue)
        }
    }

    @Test
    fun wrongMethod() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("GET")
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(405, firstValue)
        }
    }

    @Test
    fun missingCatalogType() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun wrongCatalogType() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("doesnotexist"))))
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun tooManyCatalogTypes() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("datasets", "concepts"))))
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(400, firstValue)
        }
    }

    @Test
    fun acceptsDatasets() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("datasets"))))
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsDataServices() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("dataservices"))))
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsConcepts() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("concepts"))))
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsInformationModels() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("informationmodels"))))
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsPublicServices() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("publicservices"))))
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

    @Test
    fun acceptsEvents() {
        whenever(request!!.path).thenReturn("/transform")
        whenever(request.method).thenReturn("POST")
        whenever(request.queryParameters).thenReturn(mapOf(Pair("catalog", listOf("events"))))
        DataTransformationService().service(request, response!!)

        argumentCaptor<Int>().apply {
            verify(response, times(1)).setStatusCode(capture())
            Assertions.assertEquals(202, firstValue)
        }
    }

}