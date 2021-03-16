package no.fdk.fdk_data_transformation_service.adapter

import no.fdk.fdk_data_transformation_service.rdf.parseRDFResponse
import org.apache.http.HttpHeaders
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.Lang
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

private val LOGGER: Logger = LoggerFactory.getLogger(RDFAdapter::class.java)

class RDFAdapter {

    fun getRDF(uri: String): Model? =
        with(URL(uri).openConnection() as HttpURLConnection) {
            try {
                setRequestProperty(HttpHeaders.ACCEPT, "text/turtle")

                return if (IntRange(200, 299).contains(responseCode)) {
                    inputStream.bufferedReader()
                        .use(BufferedReader::readText)
                        .let { parseRDFResponse(it, Lang.TURTLE, uri) }
                } else {
                    LOGGER.error("$uri responded with $responseCode, aborting transform")
                    null
                }

            } catch (ex: Exception) {
                LOGGER.error("Error when getting rdf from $uri", ex)
                return null
            } finally {
                disconnect()
            }
        }

}
