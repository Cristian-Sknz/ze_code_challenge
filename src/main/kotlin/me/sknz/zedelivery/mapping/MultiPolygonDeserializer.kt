package me.sknz.zedelivery.mapping

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.mongodb.client.model.geojson.MultiPolygon
import com.mongodb.client.model.geojson.PolygonCoordinates
import com.mongodb.client.model.geojson.Position

object MultiPolygonDeserializer: JsonDeserializer<MultiPolygon>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): MultiPolygon {
        val node = p.codec.readTree<JsonNode>(p)
        val polygons = node.get("coordinates").map { polygon ->
            val exterior = polygon.get(0).map { it.toPosition() }
            if (polygon.has(1)) {
                val interior = polygon.get(1).map { it.toPosition() }
                return@map PolygonCoordinates(exterior, interior)
            }

            return@map PolygonCoordinates(exterior)
        }

        return MultiPolygon(polygons)
    }

    private fun JsonNode.toPosition(): Position {
        val longitude = this.get(0).asDouble()
        val latitude = this.get(1).asDouble()
        return Position(longitude, latitude)
    }
}
