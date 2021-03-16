package no.fdk.fdk_data_transformation_service.transform

import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.enum.Environment
import no.fdk.fdk_data_transformation_service.env.*
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


fun Model.createModelOfPublishersWithOrgData(publisherURIs: Set<String>, environment: Environment): Model {
    val model = ModelFactory.createDefaultModel()
    model.setNsPrefixes(nsPrefixMap)

    publisherURIs.map { Pair(it, orgResourceForPublisher(it, environment)) }
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

fun CatalogType.uri(environment: Environment): String =
    when (this) {
        CatalogType.CONCEPTS -> "${conceptsURI(environment)}/concepts"
        CatalogType.DATASERVICES -> "${dataServicesURI(environment)}/catalogs"
        CatalogType.DATASETS -> "${datasetsURI(environment)}/catalogs"
        CatalogType.EVENTS -> "${fdkBaseURI(environment)}/api/events"
        CatalogType.INFORMATIONMODELS -> "${informationModelsURI(environment)}/catalogs"
        CatalogType.PUBLICSERVICES -> "${fdkBaseURI(environment)}/api/public-services"
    }

fun Model.orgResourceForPublisher(publisherURI: String, environment: Environment): Resource? =
    orgIdFromURI(publisherURI)
        ?.let { getResource("${organizationsURI(environment)}/organizations/$it") }

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
