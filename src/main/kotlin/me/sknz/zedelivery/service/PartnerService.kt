package me.sknz.zedelivery.service

import me.sknz.zedelivery.dto.PartnerData
import me.sknz.zedelivery.dto.distance
import me.sknz.zedelivery.model.Partner
import me.sknz.zedelivery.repository.PartnerRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class PartnerService(
    private val partners: PartnerRepository,
    private val mongo: ReactiveMongoTemplate
): IPartnerService {

    override fun createPartner(partner: PartnerData): Mono<PartnerData> {
        return partners.existsById(partner.id!!).flatMap {
            if (it) {
                return@flatMap Mono.error(ResponseStatusException(400, "Já existe um registro com este Id ${partner.id}", null))
            }

            partners.save(Partner(partner)).onErrorMap(DuplicateKeyException::class.java) {
                ResponseStatusException(400, "Já existe um registro com este documento ${partner.document}", it)
            }.map(::PartnerData)
        }
    }

    override fun deletePartnerById(partner: Long): Mono<Void> {
        return partners.existsById(partner).flatMap {
            if (it) return@flatMap partners.deleteById(partner)

            Mono.error(ResponseStatusException(404, "Não existe nenhum parceiro com o Id $partner", null))
        }
    }

    override fun findAllPartners(pageable: Pageable): Mono<Page<PartnerData>> {
        return mongo.find(Query(Criteria()).with(pageable), Partner::class.java)
            .map(::PartnerData)
            .collectList()
            .zipWith(partners.count()).map {
                PageImpl(it.t1, pageable, it.t2)
            }
    }

    override fun findPartnerById(id: Long): Mono<PartnerData> {
        return partners.findById(id).map(::PartnerData).switchIfEmpty {
            Mono.error(ResponseStatusException(404, "Não foi encontrado um parceiro com o ID solicitado.", null))
        }
    }

    override fun findPartnerByDocument(document: String): Mono<PartnerData> {
        return partners.findByDocument(document).map(::PartnerData).switchIfEmpty {
            Mono.error(ResponseStatusException(404, "Não foi encontrado um parceiro com o Documento solicitado.", null))
        }
    }

    override fun findNearbyPartners(longitude: Double, latitude: Double, kilometers: Double): Flux<PartnerData> {
        val point = GeoJsonPoint(longitude, latitude)
        if (kilometers != 0.0) {
            return partners.findNearbyPartnersWithMaxDistance(point, kilometers * 1000)
                .sort(comparePartnerGeoPoint(point))
                .map(::PartnerData)
        }

        return partners.findNearbyPartners(point)
            .sort(comparePartnerGeoPoint(point))
            .map(::PartnerData)
    }

    override fun findNearbyPartner(longitude: Double, latitude: Double, kilometers: Double): Mono<PartnerData> {
        return this.findNearbyPartners(longitude, latitude, kilometers).next().switchIfEmpty {
            Mono.error(ResponseStatusException(404, "Não há nenhum parceiro proximo desta localização", null))
        }
    }

    private fun comparePartnerGeoPoint(point: GeoJsonPoint): Comparator<Partner> {
        return Comparator { a, b ->
            val distanceA = a.address!!.distance(point)
            val distanceB = b.address!!.distance(point)

            distanceA.compareTo(distanceB)
        }
    }
}