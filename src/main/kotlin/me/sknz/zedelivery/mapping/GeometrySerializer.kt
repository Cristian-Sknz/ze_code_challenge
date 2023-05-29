package me.sknz.zedelivery.mapping

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.mongodb.client.model.geojson.Geometry

object GeometrySerializer: JsonSerializer<Geometry>() {

    override fun serialize(value: Geometry, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeRawValue(value.toJson())
    }
}
