package no.fdk.fdk_data_transformation_service.transform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.fdk.fdk_data_transformation_service.adapter.RDFAdapter
import no.fdk.fdk_data_transformation_service.adapter.SPARQLAdapter
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import no.fdk.fdk_data_transformation_service.enum.Environment

class TransformActivity : CoroutineScope by CoroutineScope(Dispatchers.Default) {


    fun initiateTransform(catalogType: CatalogType, environment: Environment) {

        launch {
            Transform(RDFAdapter(), SPARQLAdapter())
                .transformCatalogForSPARQL(catalogType, environment)
        }
    }

}