package com.example

import com.google.cloud.functions.*
import io.micronaut.gcp.function.GoogleFunctionInitializer
import jakarta.inject.*


class Function : GoogleFunctionInitializer(),
    BackgroundFunction<PubSubMessage> {

    @Inject
    lateinit var loggingService: LoggingService

    override fun accept(message: PubSubMessage, context: Context) {
        loggingService.logMessage(message)
    }
}

class PubSubMessage {
    var data: String? = null
    var attributes: Map<String, String>? = null
    var messageId: String? = null
    var publishTime: String? = null
}
@Singleton
class LoggingService {

    fun logMessage(message: PubSubMessage) {
        // log the message
    }

}