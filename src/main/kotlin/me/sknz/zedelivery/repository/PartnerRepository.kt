package me.sknz.zedelivery.repository

import me.sknz.zedelivery.model.Partner
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PartnerRepository: ReactiveMongoRepository<Partner, Long> {
    fun findByDocument(document: String): Mono<Partner>
    @Query("{ coverageArea: { \$geoIntersects: { \$geometry: ?0 } } }")
    fun findNearbyPartners(point: GeoJsonPoint): Flux<Partner>
    @Query("{ address: { \$nearSphere: { \$geometry: ?0, \$maxDistance: ?1 } }, coverageArea: { \$geoIntersects: { \$geometry: ?0 } } }")
    fun findNearbyPartnersWithMaxDistance(address: GeoJsonPoint, distance: Double): Flux<Partner>
}