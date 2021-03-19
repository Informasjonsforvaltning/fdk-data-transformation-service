package no.fdk.fdk_data_transformation_service

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.enum.Environment
import no.fdk.fdk_data_transformation_service.transform.TransformActivity
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
                TransformActivity().initiateTransform(request.catalogType()!!, request.environment()!!)
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
    val environments = queryParameters["environment"]
    val environment: String = environments?.firstOrNull()?.toUpperCase() ?: ""

    return when {
        catalogTypes?.size != 1 -> true
        CatalogType.values().map { it.name }.doesNotContain(catalogType) -> true
        environments?.size != 1 -> true
        Environment.values().map { it.name }.doesNotContain(environment) -> true
        else -> false
    }
}

private fun HttpRequest.catalogType(): CatalogType? =
    queryParameters["catalog"]
        ?.firstOrNull()
        ?.toUpperCase()
        ?.let { CatalogType.valueOf(it) }

private fun HttpRequest.environment(): Environment? =
    queryParameters["environment"]
        ?.firstOrNull()
        ?.toUpperCase()
        ?.let { Environment.valueOf(it) }
