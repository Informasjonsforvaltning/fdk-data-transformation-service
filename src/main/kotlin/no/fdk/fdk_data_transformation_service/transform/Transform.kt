package no.fdk.fdk_data_transformation_service.transform

import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.enum.Environment
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
    fun transformCatalogForSPARQL(catalogType: CatalogType, environment: Environment) {
        LOGGER.debug("Starting sparql-transform of $catalogType")
        val orgURI = "${UriType.ORGANIZATION.uri(environment)}/organizations"
        val orgs = RDFDataMgr.loadModel(orgURI, Lang.TURTLE)
        val catalog = RDFDataMgr.loadModel(catalogType.uri(environment), Lang.TURTLE)

        val transformedCatalog = orgs?.createModelOfPublishersWithOrgData(
            catalog?.extractInadequatePublishers() ?: emptySet(), environment)
            ?.let { catalog?.union(it) }

        when {
            catalog == null -> LOGGER.error("Unable to get RDF-data from ${catalogType.uri(environment)}")
            orgs == null -> {
                LOGGER.error("Unable to get organizations, updating fdk-sparql-service with untransformed $catalogType-catalog")
                sparqlAdapter.updateGraph(catalog, catalogType, environment)
            }
            transformedCatalog == null -> {
                LOGGER.error("Transform of $catalogType failed, updating fdk-sparql-service with untransformed catalog")
                sparqlAdapter.updateGraph(catalog, catalogType, environment)
            }
            else -> {
                LOGGER.debug("Transform of $catalogType complete")
                sparqlAdapter.updateGraph(transformedCatalog, catalogType, environment)
            }
        }
    }

}
