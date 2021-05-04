package no.fdk.fdk_data_transformation_service.rdf

import org.apache.jena.rdf.model.Property
import org.apache.jena.rdf.model.ResourceFactory

class FDK {

    companion object {
        const val uri =
            "https://raw.githubusercontent.com/Informasjonsforvaltning/fdk-data-transformation-service/master/src/main/resources/ontology/fdk.owl#"

        val isOpenData: Property = ResourceFactory.createProperty("${uri}isOpenData")
        val isAuthoritative: Property = ResourceFactory.createProperty("${uri}isAuthoritative")
        val isRelatedToTransportportal: Property = ResourceFactory.createProperty("${uri}isRelatedToTransportportal")
    }

}
