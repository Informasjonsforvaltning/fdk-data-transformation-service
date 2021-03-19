package no.fdk.fdk_data_transformation_service.adapter

import no.fdk.fdk_data_transformation_service.config.ApplicationURI
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.rdf.createRDFResponse
import org.apache.http.HttpHeaders
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.Lang
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

private val LOGGER: Logger = LoggerFactory.getLogger(SPARQLAdapter::class.java)

private fun CatalogType.graphName() =
    when(this) {
        CatalogType.CONCEPTS -> "https://concepts.fellesdatakatalog.digdir.no"
        CatalogType.DATASERVICES -> "https://dataservices.fellesdatakatalog.digdir.no"
        CatalogType.DATASETS -> "https://datasets.fellesdatakatalog.digdir.no"
        CatalogType.EVENTS -> "https://data.norge.no/api/events"
        CatalogType.INFORMATIONMODELS -> "https://informationmodels.fellesdatakatalog.digdir.no"
        CatalogType.PUBLICSERVICES -> "https://data.norge.no/api/public-services"
    }

@Service
class SPARQLAdapter(private val uris: ApplicationURI) {

    fun updateGraph(model: Model, catalogType: CatalogType) {
        val graphName = catalogType.graphName()
        LOGGER.debug("Updating graph '$graphName' in fdk-sparql-service")
        val sparqlURI = "${uris.sparqlservice}/fuseki/harvested?graph=$graphName"
        with(URL(sparqlURI).openConnection() as HttpURLConnection) {
            try {

                setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/rdf+xml")
                requestMethod = "PUT"
                doOutput = true

                OutputStreamWriter(outputStream).use {
                    it.write(model.createRDFResponse(Lang.RDFXML))
                    it.flush()
                }

                if (IntRange(200, 299).contains(responseCode)) {
                    LOGGER.info("Graph '$graphName' updated, status: $responseCode")
                } else {
                    LOGGER.error("Update of graph '$graphName' failed, status: $responseCode")
                }
            } catch (ex: Exception) {
                LOGGER.error("Error when updating graph '$graphName'", ex)
            } finally {
                disconnect()
            }
        }
    }

}
