package no.fdk.fdk_data_transformation_service

import no.fdk.fdk_data_transformation_service.config.ApplicationURI
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(ApplicationURI::class)
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
