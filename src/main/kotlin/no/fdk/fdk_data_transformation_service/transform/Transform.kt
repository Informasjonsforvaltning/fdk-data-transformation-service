package no.fdk.fdk_data_transformation_service.transform

import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.adapter.SPARQLAdapter
import no.fdk.fdk_data_transformation_service.enum.UriType
import no.fdk.fdk_data_transformation_service.env.*
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val LOGGER: Logger = LoggerFactory.getLogger(Transform::class.java)

class Transform(
    private val sparqlAdapter: SPARQLAdapter
) {
    fun transformCatalogForSPARQL(catalogType: CatalogType) {
        LOGGER.debug("Starting sparql-transform of $catalogType")
        val orgURI = "${UriType.ORGANIZATION.uri()}/organizations"
        val orgs = RDFDataMgr.loadModel(orgURI, Lang.TURTLE)
        val catalog = RDFDataMgr.loadModel(catalogType.uri(), Lang.TURTLE)

        val transformedCatalog = orgs?.createModelOfPublishersWithOrgData(
            catalog?.extractInadequatePublishers() ?: emptySet())
            ?.let { catalog?.union(it) }

        when {
            catalog == null -> LOGGER.error("Unable to get RDF-data from ${catalogType.uri()}")
            orgs == null -> {
                LOGGER.error("Unable to get organizations, updating fdk-sparql-service with untransformed $catalogType-catalog")
                sparqlAdapter.updateGraph(catalog, catalogType)
            }
            transformedCatalog == null -> {
                LOGGER.error("Transform of $catalogType failed, updating fdk-sparql-service with untransformed catalog")
                sparqlAdapter.updateGraph(catalog, catalogType)
            }
            else -> {
                LOGGER.debug("Transform of $catalogType complete")
                sparqlAdapter.updateGraph(transformedCatalog, catalogType)
            }
        }
    }

}
