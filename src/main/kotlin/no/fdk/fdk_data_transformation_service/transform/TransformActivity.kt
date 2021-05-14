package no.fdk.fdk_data_transformation_service.transform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import org.springframework.stereotype.Component


@Component
class TransformActivity(
    private val transform: TransformService
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    fun initiateTransform(key: String) =
        catalogTypeFromRabbitMessageKey(key)
            ?.let {
                launch { transform.transformCatalogForSPARQL(it) }
            }

}
