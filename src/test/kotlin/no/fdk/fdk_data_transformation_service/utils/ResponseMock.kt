package no.fdk.fdk_data_transformation_service.utils

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import java.io.File

const val LOCAL_SERVER_PORT = 5000

private val mockserver = WireMockServer(LOCAL_SERVER_PORT)

fun startMockServer() {
    if(!mockserver.isRunning) {
        mockserver.stubFor(get(urlEqualTo("/ping"))
            .willReturn(aResponse().withStatus(200)))

        mockserver.stubFor(get(urlEqualTo("/concepts?catalogrecords=true"))
            .willReturn(ok(File("src/test/resources/concepts.ttl").readText())))
        mockserver.stubFor(get(urlEqualTo("/dataservices/catalogs?catalogrecords=true"))
            .willReturn(ok(File("src/test/resources/dataservices.ttl").readText())))
        mockserver.stubFor(get(urlEqualTo("/datasets/catalogs?catalogrecords=true"))
            .willReturn(ok(File("src/test/resources/datasets.ttl").readText())))
        mockserver.stubFor(get(urlEqualTo("/informationmodels/catalogs?catalogrecords=true"))
            .willReturn(ok(File("src/test/resources/infomodels.ttl").readText())))
        mockserver.stubFor(get(urlEqualTo("/organizations"))
            .willReturn(ok(File("src/test/resources/orgs.ttl").readText())))
        mockserver.stubFor(get(urlEqualTo("/api/events?catalogrecords=true"))
            .willReturn(ok("")))
        mockserver.stubFor(get(urlEqualTo("/api/public-services?catalogrecords=true"))
            .willReturn(ok("")))

        mockserver.stubFor(put(urlMatching(".*fuseki.*"))
            .willReturn(aResponse().withStatus(200))
        )

        mockserver.start()
    }
}

fun stopMockServer() {

    if (mockserver.isRunning) mockserver.stop()

}
