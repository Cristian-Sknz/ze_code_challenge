package me.sknz.zedelivery.mapping

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position

object PointDeserializer: JsonDeserializer<Point>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Point {
        val node = p.codec.readTree<JsonNode>(p)
        val longitude = node.get("coordinates").get(0).asDouble()
        val latitude = node.get("coordinates").get(1).asDouble()

        return Point(Position(longitude, latitude))
    }
}
