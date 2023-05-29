package me.sknz.zedelivery.mapping

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

/**
 * ## DocumentDeserializer
 *
 * Esta classe serve para serializar campos ([String]) que recebem um
 * documento com mascara. A mascara é retirada do campo deixando apenas os numeros.
 *
 * @see me.sknz.zedelivery.model.Partner.document
 * @see me.sknz.zedelivery.dto.PartnerData.document
 */
object DocumentDeserializer : JsonDeserializer<String?>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): String? {
        return p.valueAsString?.replace(Regex("\\D+"), "")
    }
}