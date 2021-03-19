package no.fdk.fdk_data_transformation_service.controller

import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.enum.Environment
import no.fdk.fdk_data_transformation_service.transform.TransformActivity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transform")
class TransformController(
    private val transformActivity: TransformActivity
) {

    @PostMapping
    fun updateMetaData(
        @RequestParam(value = "catalog", required = true) catalog: CatalogType,
        @RequestParam(value = "environment", required = true) environment: Environment
    ): ResponseEntity<Void> {
        transformActivity.initiateTransform(catalog, environment)
        return ResponseEntity(HttpStatus.OK)
    }

}
