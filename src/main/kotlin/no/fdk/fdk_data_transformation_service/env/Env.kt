package no.fdk.fdk_data_transformation_service.env

import no.fdk.fdk_data_transformation_service.enum.Environment

fun organizationsURI(environment: Environment): String =
    System.getenv("${environment}_ORGANIZATION_CATALOGUE")
        ?: System.getProperty("uris.organizations", "https://organization-catalogue.staging.fellesdatakatalog.digdir.no")

fun conceptsURI(environment: Environment): String =
    System.getenv("${environment}_CONCEPT_HARVESTER_URI")
        ?: System.getProperty("uris.concepts", "https://concepts.staging.fellesdatakatalog.digdir.no")

fun dataServicesURI(environment: Environment): String =
    System.getenv("${environment}_DATASERVICE_HARVESTER_URI")
        ?: System.getProperty("uris.dataservices", "https://dataservices.staging.fellesdatakatalog.digdir.no")

fun datasetsURI(environment: Environment): String =
    System.getenv("${environment}_DATASETS_HARVESTER_URI")
        ?: System.getProperty("uris.datasets", "https://datasets.staging.fellesdatakatalog.digdir.no")

fun fdkBaseURI(environment: Environment): String =
    System.getenv("${environment}_BASE_URI")
        ?: System.getProperty("uris.fdkbase", "https://staging.fellesdatakatalog.digdir.no")

fun informationModelsURI(environment: Environment): String =
    System.getenv("${environment}_INFO_MODEL_HARVESTER_URI")
        ?: System.getProperty("uris.informationmodels", "https://informationmodels.staging.fellesdatakatalog.digdir.no")

fun sparqlServiceURI(environment: Environment): String =
    System.getenv("${environment}_SPARQL_SERVICE_URI")
        ?: System.getProperty("uris.sparqlservice", "http://localhost:3030")
