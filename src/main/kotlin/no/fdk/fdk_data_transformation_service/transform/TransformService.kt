package no.fdk.fdk_data_transformation_service.transform

import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.adapter.SPARQLAdapter
import no.fdk.fdk_data_transformation_service.config.ApplicationURI
import no.fdk.fdk_data_transformation_service.rdf.*
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.Resource
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val LOGGER: Logger = LoggerFactory.getLogger(TransformService::class.java)

@Service
class TransformService(
    private val uris: ApplicationURI,
    private val sparqlAdapter: SPARQLAdapter
) {
    fun transformCatalogForSPARQL(catalogType: CatalogType) {
        LOGGER.debug("Starting sparql-transform of $catalogType")
        try {
            val orgs = RDFDataMgr.loadModel(uris.organizations, Lang.TURTLE)
            val catalog = RDFDataMgr.loadModel(catalogType.uri(uris), Lang.TURTLE)

            if (catalogType == CatalogType.DATASETS) catalog.addFDKTags()

            val transformedCatalog = orgs?.createModelOfPublishersWithOrgData(
                catalog?.extractInadequatePublishers() ?: emptySet(), uris.organizations
            )
                ?.let { catalog?.union(it) }

            when {
                catalog == null -> LOGGER.error("Unable to get RDF-data from ${catalogType.uri(uris)}")
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
        } catch (ex: Exception) {
            LOGGER.error("Transform of $catalogType failed", ex)
        }
    }

}

private fun Model.addFDKTags() {
    setNsPrefix("fdk", FDK.uri)
    datasetsFromSelect(napDatasetsQuery)
        .forEach { it.addLiteral(FDK.isRelatedToTransportportal, true) }
    datasetsFromSelect(authoritativeDatasetsQuery)
        .forEach { it.addLiteral(FDK.isAuthoritative, true) }
    datasetsFromSelect(openDataDatasetsQuery)
        .forEach { it.addLiteral(FDK.isOpenData, true) }
}

private fun Model.datasetsFromSelect(selectQuery: String): List<Resource> {
    val query = QueryFactory.create(selectQuery)
    val datasets: MutableSet<Resource> = mutableSetOf()
    QueryExecutionFactory.create(query, this)
        .execSelect()
        .forEach { datasets.add(it.getResource("dataset")) }

    return datasets.toList()
}
