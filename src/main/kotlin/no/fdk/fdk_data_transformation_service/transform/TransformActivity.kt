package no.fdk.fdk_data_transformation_service.transform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import org.springframework.stereotype.Service


@Service
class TransformActivity(private val transform: Transform) : CoroutineScope by CoroutineScope(Dispatchers.Default) {


    fun initiateTransform(catalogType: CatalogType) {

        launch { transform.transformCatalogForSPARQL(catalogType) }
    }

}