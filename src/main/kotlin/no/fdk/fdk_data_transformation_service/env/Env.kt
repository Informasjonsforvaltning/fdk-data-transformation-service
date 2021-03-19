package no.fdk.fdk_data_transformation_service.env

import no.fdk.fdk_data_transformation_service.enum.Environment
import no.fdk.fdk_data_transformation_service.enum.UriType

fun UriType.uri(environment: Environment): String =
    System.getenv(envName(environment)) ?: System.getProperty(property(), default())

private fun UriType.envName(environment: Environment): String =
    when(this) {
        UriType.DATASETS -> "${environment}_DATASETS_HARVESTER_URI"
        UriType.DATASERVICES -> "${environment}_DATASERVICE_HARVESTER_URI"
        UriType.CONCEPTS -> "${environment}_CONCEPT_HARVESTER_URI"
        UriType.INFORMATIONMODELS -> "${environment}_INFO_MODEL_HARVESTER_URI"
        UriType.FDK -> "${environment}_BASE_URI"
        UriType.ORGANIZATION -> "${environment}_ORGANIZATION_CATALOGUE"
        UriType.SPARQL -> "${environment}_SPARQL_SERVICE_URI"
    }

private fun UriType.property(): String =
    when(this) {
        UriType.DATASETS -> "uris.datasets"
        UriType.DATASERVICES -> "uris.dataservices"
        UriType.CONCEPTS -> "uris.concepts"
        UriType.INFORMATIONMODELS -> "uris.informationmodels"
        UriType.FDK -> "uris.fdkbase"
        UriType.ORGANIZATION -> "uris.organizations"
        UriType.SPARQL -> "uris.sparqlservice"
    }

private fun UriType.default(): String =
    when(this) {
        UriType.DATASETS -> "https://datasets.staging.fellesdatakatalog.digdir.no"
        UriType.DATASERVICES -> "https://dataservices.staging.fellesdatakatalog.digdir.no"
        UriType.CONCEPTS -> "https://concepts.staging.fellesdatakatalog.digdir.no"
        UriType.INFORMATIONMODELS -> "https://informationmodels.staging.fellesdatakatalog.digdir.no"
        UriType.FDK -> "https://staging.fellesdatakatalog.digdir.no"
        UriType.ORGANIZATION -> "https://organization-catalogue.staging.fellesdatakatalog.digdir.no"
        UriType.SPARQL -> "http://localhost:3030"
    }
