package no.fdk.fdk_data_transformation_service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("application.graph")
data class ApplicationGraph(
    val datasets: String,
    val dataservices: String,
    val concepts: String,
    val informationmodels: String,
    val events: String,
    val publicservices: String
)
