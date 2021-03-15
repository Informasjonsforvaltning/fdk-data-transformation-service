package no.fdk.fdk_data_transformation_service

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import no.fdk.fdk_data_transformation_service.rdf.createRDFResponse
import no.fdk.fdk_data_transformation_service.rdf.headerFromJenaLang
import no.fdk.fdk_data_transformation_service.rdf.jenaLangFromHeader
import no.fdk.fdk_data_transformation_service.rdf.parseRDF
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.Lang
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection

val LOGGER: Logger = LoggerFactory.getLogger(DataTransformationService::class.java)
val ENDPOINTS = listOf("/transform")

class DataTransformationService : HttpFunction {

    override fun service(request: HttpRequest, response: HttpResponse) {
        val contentLang = jenaLangFromHeader(request.headers["Content-Type"]?.firstOrNull())
        val acceptLang = jenaLangFromHeader(request.headers["Accept"]?.firstOrNull())
        when {
            !ENDPOINTS.contains(request.path) -> response.setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            request.method != "POST" -> response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD)
            contentLang == null -> response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE)
            contentLang == Lang.RDFNULL -> response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE)
            acceptLang == Lang.RDFNULL -> response.setStatusCode(HttpURLConnection.HTTP_NOT_ACCEPTABLE)
            else -> parseRDF(request.reader.readText(), contentLang)
                ?.run { enrichPublishersData(acceptLang, response) }
                ?: run { response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST) }
        }
    }
}

private fun Model.enrichPublishersData(acceptLang: Lang?, response: HttpResponse) {
    val contentLang = acceptLang ?: Lang.TURTLE
    response.setContentType(headerFromJenaLang(contentLang))

    response.writer.write(createRDFResponse(contentLang))
}
