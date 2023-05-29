package me.sknz.zedelivery.model

import me.sknz.zedelivery.dto.PartnerData
import me.sknz.zedelivery.dto.toGeoJson
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("partners")
class Partner() {

    @Id
    var id: Long? = null
    var tradingName: String? = null
    var ownerName: String? = null

    @Indexed(unique = true)
    var document: String? = null
    var coverageArea: GeoJsonMultiPolygon? = null

    @GeoSpatialIndexed(name="address", type = GeoSpatialIndexType.GEO_2DSPHERE)
    var address: GeoJsonPoint? = null

    constructor(partnerData: PartnerData): this() {
        this.id = partnerData.id
        this.address = partnerData.address!!.toGeoJson()
        this.ownerName = partnerData.ownerName
        this.document = partnerData.document
        this.coverageArea = partnerData.coverageArea!!.toGeoJson()
        this.tradingName = partnerData.tradingName
    }

    override fun toString(): String {
        return "Partner(id=$id, tradingName=$tradingName, ownerName=$ownerName, document=$document, coverageArea=$coverageArea, address=$address)"
    }
}