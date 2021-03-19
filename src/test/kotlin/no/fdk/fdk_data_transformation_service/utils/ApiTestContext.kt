package no.fdk.fdk_data_transformation_service.utils

import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

abstract class ApiTestContext {

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
