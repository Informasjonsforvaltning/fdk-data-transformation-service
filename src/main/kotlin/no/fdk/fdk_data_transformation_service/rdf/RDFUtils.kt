package no.fdk.fdk_data_transformation_service.rdf

import org.apache.jena.rdf.model.*
import org.apache.jena.riot.Lang
import java.io.ByteArrayOutputStream


fun Model.createRDFResponse(responseType: Lang): String =
    ByteArrayOutputStream().use { out ->
        write(out, responseType.name)
        out.flush()
        out.toString("UTF-8")
    }

fun Statement.isResourceProperty(): Boolean =
    try {
        resource.isResource
    } catch (ex: ResourceRequiredException) {
        false
    }

fun Resource.safeAddProperty(property: Property, value: RDFNode?): Resource =
    if (value == null) this
    else addProperty(property, value)

val openDataURIs: Set<String> = setOf(
    "http://creativecommons.org/licenses/by/4.0/deed.no",
    "http://creativecommons.org/licenses/by/4.0/deed.no/",
    "https://creativecommons.org/licenses/by/4.0/deed.no",
    "https://creativecommons.org/licenses/by/4.0/deed.no/",
    "http://data.norge.no/nlod/no/1.0",
    "http://data.norge.no/nlod/no/1.0/",
    "https://data.norge.no/nlod/no/1.0",
    "https://data.norge.no/nlod/no/1.0/",
    "http://creativecommons.org/publicdomain/zero/1.0",
    "http://creativecommons.org/publicdomain/zero/1.0/",
    "https://creativecommons.org/publicdomain/zero/1.0",
    "https://creativecommons.org/publicdomain/zero/1.0/",
    "http://data.norge.no/nlod/no/2.0",
    "http://data.norge.no/nlod/no/2.0/",
    "https://data.norge.no/nlod/no/2.0",
    "https://data.norge.no/nlod/no/2.0/",
    "http://creativecommons.org/licenses/by/4.0",
    "http://creativecommons.org/licenses/by/4.0/",
    "https://creativecommons.org/licenses/by/4.0",
    "https://creativecommons.org/licenses/by/4.0/",
    "http://data.norge.no/nlod/no",
    "http://data.norge.no/nlod/no/",
    "https://data.norge.no/nlod/no",
    "https://data.norge.no/nlod/no/",
    "http://data.norge.no/nlod",
    "http://data.norge.no/nlod/",
    "https://data.norge.no/nlod",
    "https://data.norge.no/nlod/")

val napThemes: Set<String> = setOf(
    "https://psi.norge.no/los/tema/mobilitetstilbud",
    "https://psi.norge.no/los/tema/trafikkinformasjon",
    "https://psi.norge.no/los/tema/veg-og-vegregulering",
    "https://psi.norge.no/los/tema/yrkestransport",
    "https://psi.norge.no/los/ord/ruteinformasjon",
    "https://psi.norge.no/los/ord/lokasjonstjenester",
    "https://psi.norge.no/los/ord/tilrettelagt-transport",
    "https://psi.norge.no/los/ord/miljovennlig-transport",
    "https://psi.norge.no/los/ord/takster-og-kjopsinformasjon",
    "https://psi.norge.no/los/ord/reisegaranti",
    "https://psi.norge.no/los/ord/reisebillett",
    "https://psi.norge.no/los/ord/parkering-og-hvileplasser",
    "https://psi.norge.no/los/ord/drivstoff-og-ladestasjoner",
    "https://psi.norge.no/los/ord/skoleskyss",
    "https://psi.norge.no/los/ord/ruteplanlegger",
    "https://psi.norge.no/los/ord/veg--og-foreforhold",
    "https://psi.norge.no/los/ord/sanntids-trafikkinformasjon",
    "https://psi.norge.no/los/ord/bominformasjon",
    "https://psi.norge.no/los/ord/trafikksignaler-og-reguleringer",
    "https://psi.norge.no/los/ord/vegarbeid",
    "https://psi.norge.no/los/ord/trafikksikkerhet",
    "https://psi.norge.no/los/ord/persontransport",
    "https://psi.norge.no/los/ord/godstransport",
    "https://psi.norge.no/los/ord/feiing-og-stroing",
    "https://psi.norge.no/los/ord/aksellastrestriksjoner",
    "https://psi.norge.no/los/ord/broyting",
    "https://psi.norge.no/los/ord/gangveg",
    "https://psi.norge.no/los/ord/vegnett",
    "https://psi.norge.no/los/ord/gatelys",
    "https://psi.norge.no/los/ord/vegbygging",
    "https://psi.norge.no/los/ord/privat-vei",
    "https://psi.norge.no/los/ord/vegvedlikehold",
    "https://psi.norge.no/los/ord/gravemelding",
    "https://psi.norge.no/los/ord/sykkel")
