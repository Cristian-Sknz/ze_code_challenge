package me.sknz.zedelivery.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.sknz.zedelivery.dto.PartnerData
import me.sknz.zedelivery.model.Partner
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.core.publisher.Mono

object PartnerUtils {

    fun getResourcePartner(): MutableList<Partner> {
        val mapper = ObjectMapper()
        val resource = ClassPathResource("pdvs.json").inputStream
        val pdvs = mapper.readValue<MutableList<PartnerData>>(mapper.readTree(resource).get("pdvs").toString())

        return pdvs.map(::Partner).toMutableList()
    }

    fun populate(template: ReactiveMongoTemplate) {
        getResourcePartner()
            .map(template::save)
            .forEach(Mono<Partner>::block)
    }
}