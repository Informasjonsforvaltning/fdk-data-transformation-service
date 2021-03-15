package no.fdk.fdk_data_transformation_service.rdf

import no.fdk.fdk_data_transformation_service.LOGGER
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.Lang
import java.io.ByteArrayOutputStream
import java.io.StringReader

const val BACKUP_BASE_URI = "http://example.com/"

fun jenaLangFromHeader(header: String?): Lang? =
    when {
        header == null -> null
        header.contains("text/turtle") -> Lang.TURTLE
        header.contains("application/rdf+xml") -> Lang.RDFXML
        header.contains("application/rdf+json") -> Lang.RDFJSON
        header.contains("application/ld+json") -> Lang.JSONLD
        header.contains("application/n-triples") -> Lang.NTRIPLES
        header.contains("text/n3") -> Lang.N3
        header.contains("*/*") -> null
        else -> Lang.RDFNULL
    }

fun headerFromJenaLang(lang: Lang): String =
    when(lang) {
        Lang.TURTLE -> "text/turtle;charset=UTF-8"
        Lang.RDFXML -> "application/rdf+xml;charset=UTF-8"
        Lang.RDFJSON -> "application/rdf+json;charset=UTF-8"
        Lang.JSONLD -> "application/ld+json;charset=UTF-8"
        Lang.NTRIPLES -> "application/n-triples;charset=UTF-8"
        Lang.N3 -> "text/n3;charset=UTF-8"
        else -> "text/turtle;charset=UTF-8"
    }

fun parseRDF(body: String, language: Lang): Model? {
    val model = ModelFactory.createDefaultModel()

    try {
        model.read(StringReader(body), BACKUP_BASE_URI, language.name)
    } catch (ex: Exception) {
        LOGGER.error("Parse failed: ${ex.message}")
        return null
    }

    return model
}

fun Model.createRDFResponse(responseType: Lang): String =
    ByteArrayOutputStream().use { out ->
        write(out, responseType.name)
        out.flush()
        out.toString("UTF-8")
    }