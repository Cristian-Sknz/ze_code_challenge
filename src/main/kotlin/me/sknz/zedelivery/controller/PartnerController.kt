package me.sknz.zedelivery.controller

import me.sknz.zedelivery.dto.PartnerData
import me.sknz.zedelivery.service.IPartnerService
import org.reactivestreams.Publisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/partner")
class PartnerController(private val service: IPartnerService) {

    companion object {
        const val DEFAULT_NEARBY_KILOMETERS = 0.0
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getPartners(pageable: Pageable?): Mono<Page<PartnerData>> {
        return service.findAllPartners(pageable ?: Pageable.ofSize(20))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPartner(@RequestBody @Validated partner: PartnerData): Mono<PartnerData> {
        return service.createPartner(partner)
    }

    @DeleteMapping("/{partner}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePartner(@PathVariable partner: Long): Mono<Void> {
        return service.deletePartnerById(partner)
    }

    @GetMapping("/{partner}")
    @ResponseStatus(HttpStatus.OK)
    fun getPartnerByIdOrDocument(@PathVariable partner: String,
                                 @RequestParam("type") type: String?): Mono<PartnerData> {
        if (type.equals("Document", true)) {
            return service.findPartnerByDocument(partner.replace(Regex("\\D+"), ""))
        }

        val id = partner.toLongOrNull()
            ?: return Mono.error(ResponseStatusException(400, "Este Id está incorreto", null))

        return service.findPartnerById(id)
    }

    @GetMapping("/nearby")
    @ResponseStatus(HttpStatus.OK)
    fun getNearbyPartner(@RequestParam("longitude") long: Double,
                         @RequestParam("latitude") lat: Double,
                         @RequestParam("kilometers", required = false) km: Double?,
                         @RequestParam("type", required = false) type: String?): Publisher<PartnerData> {
        if (type.equals("all", true)) {
            return service.findNearbyPartners(long, lat, km ?: DEFAULT_NEARBY_KILOMETERS)
        }

        return service.findNearbyPartner(long, lat, km ?: DEFAULT_NEARBY_KILOMETERS)
    }
}