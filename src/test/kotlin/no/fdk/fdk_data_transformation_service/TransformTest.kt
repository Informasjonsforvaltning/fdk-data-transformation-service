package no.fdk.fdk_data_transformation_service

import com.nhaarman.mockitokotlin2.*
import no.fdk.fdk_data_transformation_service.adapter.RDFAdapter
import no.fdk.fdk_data_transformation_service.adapter.SPARQLAdapter
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.enum.Environment
import no.fdk.fdk_data_transformation_service.transform.Transform
import no.fdk.fdk_data_transformation_service.utils.TestResponseReader
import org.apache.jena.rdf.model.Model
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransformTest {
    private val rdfAdapter: RDFAdapter = mock()
    private val sparqlAdapter: SPARQLAdapter = mock()
    private val transformer = Transform(rdfAdapter, sparqlAdapter)

    private val responseReader = TestResponseReader()

    @BeforeEach
    fun beforeTests() {
        reset(sparqlAdapter, rdfAdapter)
        whenever(rdfAdapter.getRDF("https://concepts.staging.fellesdatakatalog.digdir.no/concepts"))
            .thenReturn(responseReader.parseTurtleFile("concepts.ttl"))
        whenever(rdfAdapter.getRDF("https://dataservices.staging.fellesdatakatalog.digdir.no/catalogs"))
            .thenReturn(responseReader.parseTurtleFile("dataservices.ttl"))
        whenever(rdfAdapter.getRDF("https://datasets.staging.fellesdatakatalog.digdir.no/catalogs"))
            .thenReturn(responseReader.parseTurtleFile("datasets.ttl"))
        whenever(rdfAdapter.getRDF("https://informationmodels.staging.fellesdatakatalog.digdir.no/catalogs"))
            .thenReturn(responseReader.parseTurtleFile("infomodels.ttl"))
        whenever(rdfAdapter.getRDF("https://organization-catalogue.staging.fellesdatakatalog.digdir.no/organizations"))
            .thenReturn(responseReader.parseTurtleFile("orgs.ttl"))
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
