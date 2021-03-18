package no.fdk.fdk_data_transformation_service

import com.nhaarman.mockitokotlin2.*
import no.fdk.fdk_data_transformation_service.adapter.SPARQLAdapter
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.enum.Environment
import no.fdk.fdk_data_transformation_service.transform.Transform
import no.fdk.fdk_data_transformation_service.utils.ApiTestContext
import no.fdk.fdk_data_transformation_service.utils.TestResponseReader
import org.apache.jena.rdf.model.Model
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransformTest: ApiTestContext() {
    private val sparqlAdapter: SPARQLAdapter = mock()
    private val transformer = Transform(sparqlAdapter)

    private val responseReader = TestResponseReader()

    @BeforeEach
    fun resetMock() {
        reset(sparqlAdapter)
    }

    @Test
    fun testConcepts() {
        transformer.transformCatalogForSPARQL(CatalogType.CONCEPTS, Environment.STAGING)

        val expected = responseReader.parseTurtleFile("transformed_concepts.ttl")

        argumentCaptor<Model, CatalogType, Environment>().apply {
            verify(sparqlAdapter, times(1)).updateGraph(first.capture(), second.capture(), third.capture())
            assertTrue(expected.isIsomorphicWith(first.firstValue))
            Assertions.assertEquals(CatalogType.CONCEPTS, second.firstValue)
            Assertions.assertEquals(Environment.STAGING, third.firstValue)
        }
    }

    @Test
    fun testDataServices() {
        transformer.transformCatalogForSPARQL(CatalogType.DATASERVICES, Environment.STAGING)

        val expected = responseReader.parseTurtleFile("transformed_dataservices.ttl")

        argumentCaptor<Model, CatalogType, Environment>().apply {
            verify(sparqlAdapter, times(1)).updateGraph(first.capture(), second.capture(), third.capture())
            assertTrue(expected.isIsomorphicWith(first.firstValue))
            Assertions.assertEquals(CatalogType.DATASERVICES, second.firstValue)
            Assertions.assertEquals(Environment.STAGING, third.firstValue)
        }
    }

    @Test
    fun testDatasets() {
        transformer.transformCatalogForSPARQL(CatalogType.DATASETS, Environment.STAGING)

        val expected = responseReader.parseTurtleFile("transformed_datasets.ttl")

        argumentCaptor<Model, CatalogType, Environment>().apply {
            verify(sparqlAdapter, times(1)).updateGraph(first.capture(), second.capture(), third.capture())
            assertTrue(expected.isIsomorphicWith(first.firstValue))
            Assertions.assertEquals(CatalogType.DATASETS, second.firstValue)
            Assertions.assertEquals(Environment.STAGING, third.firstValue)
        }
    }

    @Test
    fun testInformationModels() {
        transformer.transformCatalogForSPARQL(CatalogType.INFORMATIONMODELS, Environment.STAGING)

        val expected = responseReader.parseTurtleFile("transformed_infomodels.ttl")

        argumentCaptor<Model, CatalogType, Environment>().apply {
            verify(sparqlAdapter, times(1)).updateGraph(first.capture(), second.capture(), third.capture())
            assertTrue(expected.isIsomorphicWith(first.firstValue))
            Assertions.assertEquals(CatalogType.INFORMATIONMODELS, second.firstValue)
            Assertions.assertEquals(Environment.STAGING, third.firstValue)
        }
    }

}
