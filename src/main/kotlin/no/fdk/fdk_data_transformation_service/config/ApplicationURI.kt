package no.fdk.fdk_data_transformation_service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("application.uri")
data class ApplicationURI(
    val datasets: String,
    val dataservices: String,
    val concepts: String,
    val informationmodels: String,
    val events: String,
    val organizations: String,
    val publicservices: String,
    val sparqlservice: String
)
