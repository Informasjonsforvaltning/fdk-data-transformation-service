package no.fdk.fdk_data_transformation_service.transform

import no.fdk.fdk_data_transformation_service.config.ApplicationURI
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.rdf.BR
import no.fdk.fdk_data_transformation_service.rdf.isResourceProperty
import no.fdk.fdk_data_transformation_service.rdf.safeAddProperty
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.RDFNode
import org.apache.jena.rdf.model.Resource
import org.apache.jena.sparql.vocabulary.FOAF
import org.apache.jena.vocabulary.DCTerms
import org.apache.jena.vocabulary.RDF
import org.apache.jena.vocabulary.ROV
import org.apache.jena.vocabulary.SKOS

const val RECORDS_PARAM_TRUE = "catalogrecords=true"

fun Model.createModelOfPublishersWithOrgData(publisherURIs: Set<String>, orgsURI: String): Model {
    val model = ModelFactory.createDefaultModel()
    model.setNsPrefixes(nsPrefixMap)

    publisherURIs.map { Pair(it, orgResourceForPublisher(it, orgsURI)) }
        .filter { it.second != null }
        .forEach {
            model.createResource(it.first).addPropertiesFromOrgResource(it.second)
        }

    return model
}

fun Resource.addPropertiesFromOrgResource(orgResource: Resource?) {
    if (orgResource != null) {
        safeAddProperty(RDF.type, orgResource.getProperty(RDF.type)?.`object`)
        safeAddProperty(DCTerms.identifier, orgResource.getProperty(DCTerms.identifier)?.`object`)
        safeAddProperty(BR.orgPath, orgResource.getProperty(BR.orgPath)?.`object`)
        safeAddProperty(ROV.legalName, orgResource.getProperty(ROV.legalName)?.`object`)
        safeAddProperty(FOAF.name, orgResource.getProperty(FOAF.name)?.`object`)
        addOrgType(orgResource)
    }
}

private fun Resource.addOrgType(orgResource: Resource): Resource {
    val orgType = orgResource.getProperty(ROV.orgType)
    if (orgType != null && orgType.isResourceProperty()) {
        orgType.resource
            .getProperty(SKOS.prefLabel)
            ?.`object`
            ?.let {
                addProperty(
                    ROV.orgType,
                    model.createResource(SKOS.Concept)
                        .addProperty(SKOS.prefLabel, it))
            }
    }

    return this
}

fun Model.extractInadequatePublishers(): Set<String> =
    listResourcesWithProperty(DCTerms.publisher)
        .toList()
        .flatMap { it.listProperties(DCTerms.publisher).toList() }
        .asSequence()
        .filter { it.isResourceProperty() }
        .map { it.resource }
        .filter {  it.isURIResource }
        .filter { it.dctIdentifierIsInadequate() }
        .mapNotNull { it.uri }
        .toSet()

fun Resource.dctIdentifierIsInadequate(): Boolean =
    listProperties(DCTerms.identifier)
        .toList()
        .map { it.`object` }
        .mapNotNull { it.extractPublisherId() }
        .isNullOrEmpty()

fun CatalogType.uri(uris: ApplicationURI): String =
    when (this) {
        CatalogType.CONCEPTS -> "${uris.concepts}?$RECORDS_PARAM_TRUE"
        CatalogType.DATASERVICES -> "${uris.dataservices}?$RECORDS_PARAM_TRUE"
        CatalogType.DATASETS -> "${uris.datasets}?$RECORDS_PARAM_TRUE"
        CatalogType.EVENTS -> "${uris.events}?$RECORDS_PARAM_TRUE"
        CatalogType.INFORMATIONMODELS -> "${uris.informationmodels}?$RECORDS_PARAM_TRUE"
        CatalogType.PUBLICSERVICES -> "${uris.publicservices}?$RECORDS_PARAM_TRUE"
    }

fun catalogTypeFromRabbitMessageKey(key: String): CatalogType? =
    when {
        key.contains("concepts") -> CatalogType.CONCEPTS
        key.contains("dataservices") -> CatalogType.DATASERVICES
        key.contains("datasets") -> CatalogType.DATASETS
        key.contains("informationmodels") -> CatalogType.INFORMATIONMODELS
        key.contains("events") -> CatalogType.EVENTS
        key.contains("public_services") -> CatalogType.PUBLICSERVICES
        else -> null
    }

fun Model.orgResourceForPublisher(publisherURI: String, orgsURI: String): Resource? =
    orgIdFromURI(publisherURI)
        ?.let { getResource("$orgsURI/$it") }

fun RDFNode.extractPublisherId(): String? =
    when {
        isURIResource -> orgIdFromURI(asResource().uri)
        isLiteral -> orgIdFromURI(asLiteral().string)
        else -> null
    }

fun orgIdFromURI(uri: String): String? {
    val regex = Regex("""[0-9]{9}""")
    val allMatching = regex.findAll(uri).toList()

    return if (allMatching.size == 1) allMatching.first().value
    else null
}
