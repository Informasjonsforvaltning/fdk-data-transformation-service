package no.fdk.fdk_data_transformation_service.rdf

import no.fdk.fdk_data_transformation_service.LOGGER
import org.apache.jena.rdf.model.*
import org.apache.jena.riot.Lang
import java.io.ByteArrayOutputStream
import java.io.StringReader

const val BACKUP_BASE_URI = "http://example.com/"

fun parseRDFResponse(responseBody: String, rdfLanguage: Lang, rdfSource: String?): Model? {
    val responseModel = ModelFactory.createDefaultModel()

    try {
        responseModel.read(StringReader(responseBody), BACKUP_BASE_URI, rdfLanguage.name)
    } catch (ex: Exception) {
        LOGGER.error("Parse from $rdfSource has failed: ${ex.message}")
        return null
    }

    return responseModel
}

fun Model.createRDFResponse(responseType: Lang): String =
    ByteArrayOutputStream().use { out ->
        write(out, responseType.name)
        out.flush()
        out.toString("UTF-8")
    }

fun Statement.isResourceProperty(): Boolean =
    try {
        resource.isResource
    } catch (ex: ResourceRequiredException) {
        false
    }

fun Resource.safeAddProperty(property: Property, value: RDFNode?): Resource =
    if (value == null) this
    else addProperty(property, value)
