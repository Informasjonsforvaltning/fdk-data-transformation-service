package no.fdk.fdk_data_transformation_service

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse

class DataTransformationService : HttpFunction {

    override fun service(request: HttpRequest, response: HttpResponse) {
        val writer = response.writer
        writer.write("hello function test")
    }

}
