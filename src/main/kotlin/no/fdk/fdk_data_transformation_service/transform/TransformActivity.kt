package no.fdk.fdk_data_transformation_service.transform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.fdk.fdk_data_transformation_service.enum.CatalogType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

private val transformQueue: Queue<CatalogType> = LinkedList()
private val lastTransforms: MutableMap<CatalogType, LocalDateTime> = mutableMapOf()

@Component
class TransformActivity(
    private val transform: TransformService
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    @Scheduled(fixedRate = 10000)
    private fun pollQueue() =
        transformQueue.poll()
            ?.launchIfNotLaunchedRecently()

    fun initiateTransform(key: String) =
        catalogTypeFromRabbitMessageKey(key)
            ?.queueTransform()

    private fun CatalogType.queueTransform() {
        if (!transformQueue.contains(this)) transformQueue.add(this)
    }

    private fun CatalogType.launchIfNotLaunchedRecently() {
        val lastTransform = lastTransforms[this]
        when {
            lastTransform == null -> launchTransform(this)
            lastTransform.isBefore(LocalDateTime.now().minusMinutes(15)) -> launchTransform(this)
            else -> queueTransform()
        }
    }

    private fun launchTransform(type: CatalogType) {
        lastTransforms[type] = LocalDateTime.now()
        launch { transform.transformCatalogForSPARQL(type) }
    }

}
