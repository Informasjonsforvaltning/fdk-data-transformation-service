package no.fdk.fdk_data_transformation_service.enum

enum class CatalogType {
    DATASETS,
    DATASERVICES,
    CONCEPTS,
    INFORMATIONMODELS,
    EVENTS,
    PUBLICSERVICES
}

enum class Environment {
    STAGING,
    DEMO,
    PROD,
}

enum class UriType {
    DATASETS,
    DATASERVICES,
    CONCEPTS,
    INFORMATIONMODELS,
    FDK,
    ORGANIZATION,
    SPARQL
}
