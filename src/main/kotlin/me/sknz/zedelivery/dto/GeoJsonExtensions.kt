package me.sknz.zedelivery.dto

import com.mongodb.client.model.geojson.MultiPolygon
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.PolygonCoordinates
import com.mongodb.client.model.geojson.Position
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import kotlin.math.sqrt

fun MultiPolygon.toGeoJson(): GeoJsonMultiPolygon {
    return GeoJsonMultiPolygon(this.coordinates.map { it.toGeoJson() })
}

fun PolygonCoordinates.toGeoJson(): GeoJsonPolygon {
    return GeoJsonPolygon(this.exterior.map { GeoJsonPoint(it.values[0], it.values[1]) })
}

fun Point.toGeoJson(): GeoJsonPoint {
    return GeoJsonPoint(this.coordinates.values[0], this.coordinates.values[1])
}

fun GeoJsonPoint.toPoint(): Point {
    return Point(Position(this.x, this.y))
}

fun GeoJsonPoint.distance(other: GeoJsonPoint): Double {
    val x = this.x - other.x
    val y = this.y - other.y

    return sqrt(x * x + y * y)
}

fun GeoJsonMultiPolygon.toMultiPolygon(): MultiPolygon {
    return MultiPolygon(this.coordinates.map { coordinate ->
        PolygonCoordinates(coordinate.points.map { Position(it.x, it.y) })
    })
}