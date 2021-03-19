package no.fdk.fdk_data_transformation_service.rabbit

import no.fdk.fdk_data_transformation_service.transform.TransformActivity
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

private val logger = LoggerFactory.getLogger(RabbitMQListener::class.java)

@Service
class RabbitMQListener(
    private val transformActivity: TransformActivity
) {

    @RabbitListener(queues = ["#{receiverQueue.name}"])
    fun receiveMessage(message: Message) {
        logger.info("Received message with key ${message.messageProperties.receivedRoutingKey}")

        transformActivity.initiateTransform(message.messageProperties.receivedRoutingKey)
    }

}
