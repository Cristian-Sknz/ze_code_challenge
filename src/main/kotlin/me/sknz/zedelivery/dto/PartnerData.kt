package me.sknz.zedelivery.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.mongodb.client.model.geojson.MultiPolygon
import com.mongodb.client.model.geojson.Point
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import me.sknz.zedelivery.dto.validation.CPFouCNPJ
import me.sknz.zedelivery.mapping.DocumentDeserializer
import me.sknz.zedelivery.mapping.GeometrySerializer
import me.sknz.zedelivery.mapping.MultiPolygonDeserializer
import me.sknz.zedelivery.mapping.PointDeserializer
import me.sknz.zedelivery.model.Partner

class PartnerData() {

    @NotNull
    @Positive
    var id: Long? = null

    @NotEmpty
    var tradingName: String? = null
    @NotEmpty
    var ownerName: String? = null

    @JsonDeserialize(using =  DocumentDeserializer::class)
    @CPFouCNPJ
    var document: String? = null
        set(value) {
            field = value?.replace(Regex("\\D+"), "")
        }

    @JsonDeserialize(using = MultiPolygonDeserializer::class)
    @JsonSerialize(using = GeometrySerializer::class)
    @NotNull
    var coverageArea: MultiPolygon? = null

    @JsonDeserialize(using = PointDeserializer::class)
    @JsonSerialize(using = GeometrySerializer::class)
    @NotNull
    var address: Point? = null

    constructor(partner: Partner): this() {
        id = partner.id
        address = partner.address!!.toPoint()
        ownerName = partner.ownerName
        document = partner.document
        coverageArea = partner.coverageArea!!.toMultiPolygon()
        tradingName = partner.tradingName
    }

    override fun toString(): String {
        return "PartnerData(id=$id, tradingName=$tradingName, ownerName=$ownerName, document=$document, coverageArea=$coverageArea, address=$address)"
    }
}
