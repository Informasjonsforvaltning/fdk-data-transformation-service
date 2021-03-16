package no.fdk.fdk_data_transformation_service.rdf

import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Property
import org.apache.jena.rdf.model.Resource

class BR {
    companion object {
        private val m = ModelFactory.createDefaultModel()

        val uri = "https://raw.githubusercontent.com/Informasjonsforvaltning/organization-catalogue/master/src/main/resources/ontology/organization-catalogue.owl#"

        val orgPath: Property = m.createProperty(uri + "orgPath")
    }
}
