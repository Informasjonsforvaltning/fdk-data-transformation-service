package no.fdk.fdk_data_transformation_service.utils

import org.junit.jupiter.api.BeforeAll
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

abstract class ApiTestContext {

    @BeforeAll
    fun setEnv() {
        System.setProperty("uris.concepts", "http://localhost:$LOCAL_SERVER_PORT")
        System.setProperty("uris.datasets", "http://localhost:${LOCAL_SERVER_PORT}/datasets")
        System.setProperty("uris.dataservices", "http://localhost:${LOCAL_SERVER_PORT}/dataservices")
        System.setProperty("uris.informationmodels", "http://localhost:${LOCAL_SERVER_PORT}/informationmodels")
        System.setProperty("uris.fdkbase", "http://localhost:${LOCAL_SERVER_PORT}")
        System.setProperty("uris.fdkbase", "http://localhost:${LOCAL_SERVER_PORT}")
        System.setProperty("uris.organizations", "http://localhost:${LOCAL_SERVER_PORT}")
        System.setProperty("uris.sparqlservice", "http://localhost:${LOCAL_SERVER_PORT}")
    }

    companion object {

        private val logger = LoggerFactory.getLogger(ApiTestContext::class.java)

        init {

            startMockServer()

            try {
                val con = URL("http://localhost:5000/ping").openConnection() as HttpURLConnection
                con.connect()
                if (con.responseCode != 200) {
                    logger.debug("Ping to mock server failed")
                    stopMockServer()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

}
