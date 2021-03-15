package no.fdk.fdk_data_transformation_service

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection

val LOGGER: Logger = LoggerFactory.getLogger(DataTransformationService::class.java)
val ENDPOINTS = listOf("/transform")
val PATH_METHODS = mapOf(Pair("/transform", listOf("POST")))

class DataTransformationService : HttpFunction {

    override fun service(request: HttpRequest, response: HttpResponse) =
        when {
            request.pathIsNotValid() -> response.setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            request.methodIsNotValid() -> response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD)
            request.queryParametersIsNotValid() -> response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
            else -> {
                transformCatalogForSPARQL(request.catalogType()!!)
                response.setStatusCode(HttpURLConnection.HTTP_ACCEPTED)
            }
        }

}

private fun HttpRequest.pathIsNotValid(): Boolean =
    ENDPOINTS.doesNotContain(path)

private fun HttpRequest.methodIsNotValid(): Boolean =
    PATH_METHODS[path]
        ?.doesNotContain(method)
        ?: true

private fun List<String>.doesNotContain(input: String): Boolean =
    !contains(input)

private fun HttpRequest.queryParametersIsNotValid(): Boolean {
    val catalogTypes = queryParameters["catalog"]
    val catalogType: String = catalogTypes?.firstOrNull()?.toUpperCase() ?: ""

    return when {
        catalogTypes?.size != 1 -> true
        CatalogType.values().map { it.name }.contains(catalogType) -> false
        else -> true
    }
}

private fun HttpRequest.catalogType(): CatalogType? =
    queryParameters["catalog"]
        ?.firstOrNull()
        ?.toUpperCase()
        ?.let { CatalogType.valueOf(it) }

private fun transformCatalogForSPARQL(catalogType: CatalogType) {
    LOGGER.debug("Transform catalog '$catalogType'")
}
