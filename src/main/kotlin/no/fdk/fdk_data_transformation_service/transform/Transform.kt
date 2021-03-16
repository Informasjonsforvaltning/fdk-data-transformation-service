package no.fdk.fdk_data_transformation_service.transform

import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.enum.Environment
import no.fdk.fdk_data_transformation_service.adapter.RDFAdapter
import no.fdk.fdk_data_transformation_service.adapter.SPARQLAdapter
import no.fdk.fdk_data_transformation_service.env.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val LOGGER: Logger = LoggerFactory.getLogger(Transform::class.java)

class Transform(
    private val rdfAdapter: RDFAdapter,
    private val sparqlAdapter: SPARQLAdapter
) {
    fun transformCatalogForSPARQL(catalogType: CatalogType, environment: Environment) {
        LOGGER.debug("Starting sparql-transform of $catalogType")
        val orgURI = "${organizationsURI(environment)}/organizations"
        val orgs = rdfAdapter.getRDF(orgURI)
        val catalog = rdfAdapter.getRDF(catalogType.uri(environment))

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
