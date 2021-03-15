package no.fdk.fdk_data_transformation_service

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection

val LOGGER: Logger = LoggerFactory.getLogger(DataTransformationService::class.java)
val ENDPOINTS = listOf("/transform")

class DataTransformationService : HttpFunction {

    override fun service(request: HttpRequest, response: HttpResponse) {
        val catalogTypes = request.queryParameters["catalog"]?.map { it.toUpperCase() }
        val catalogType = catalogTypes?.firstOrNull()
        when {
            !ENDPOINTS.contains(request.path) -> response.setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            request.method != "POST" -> response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD)
            catalogType.isNullOrBlank() -> response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
            catalogTypes.size > 1 -> response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
            !CatalogType.values().map { it.name }.contains(catalogType) -> response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
            else -> {
                transformCatalogForSPARQL(CatalogType.valueOf(catalogType))
                response.setStatusCode(HttpURLConnection.HTTP_ACCEPTED)
            }
        }
    }

}

private fun transformCatalogForSPARQL(catalogType: CatalogType) {
    LOGGER.info("Transform catalog '$catalogType'")
}
