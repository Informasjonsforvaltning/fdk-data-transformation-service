package no.fdk.fdk_data_transformation_service.rdf

val napDatasetsQuery = """PREFIX dct: <http://purl.org/dc/terms/>
PREFIX dcat: <http://www.w3.org/ns/dcat#>

SELECT DISTINCT ?dataset
WHERE {
    ?dataset a dcat:Dataset .
    ?dataset dct:accessRights ?rights .
    FILTER (STR(?rights) = 'http://publications.europa.eu/resource/authority/access-right/PUBLIC')
    ?dataset dcat:theme ?theme .
    FILTER (STR(?theme) IN ('${napThemes.joinToString("','")}'))
}"""

val openDataDatasetsQuery = """PREFIX dct: <http://purl.org/dc/terms/>
PREFIX dcat: <http://www.w3.org/ns/dcat#>

SELECT DISTINCT ?dataset
WHERE {
    ?dataset a dcat:Dataset .
    ?dataset dct:accessRights ?rights .
    FILTER (STR(?rights) = 'http://publications.europa.eu/resource/authority/access-right/PUBLIC')
    ?dataset dcat:distribution ?distribution .
    ?distribution dct:license ?license .
    OPTIONAL { ?license dct:source ?source . }
    BIND ( IF( EXISTS { ?license dct:source ?source . },
        ?source, ?license ) AS ?licenseSource ) .
    FILTER (STR(?licenseSource) IN ('${openDataURIs.joinToString("','")}'))
}"""

const val authoritativeDatasetsQuery = """PREFIX dct: <http://purl.org/dc/terms/>
SELECT DISTINCT ?dataset
WHERE {
    ?dataset dct:provenance ?provenance .
    FILTER (STR(?provenance) = 'http://data.brreg.no/datakatalog/provinens/nasjonal')
}"""
