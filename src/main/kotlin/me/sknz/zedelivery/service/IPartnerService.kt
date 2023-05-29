package me.sknz.zedelivery.service

import me.sknz.zedelivery.dto.PartnerData
import me.sknz.zedelivery.model.Partner
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * ## IPartnerService
 *
 * Interface com as regras de negocio da entidade [Partner],
 * podendo conter implementações de teste ou produção.
 *
 * @see PartnerService
 * @see me.sknz.zedelivery.controller.PartnerController
 */
interface IPartnerService {

    /**
     * Procura todos os parceiros de acordo com o [Pageable]
     *
     * @param pageable Parâmetros de paginação
     */
    fun findAllPartners(pageable: Pageable): Mono<Page<PartnerData>>

    /**
     * Cria um novo parceiro no banco de dados acordo com o [PartnerData]
     *
     * @param partner Objeto de transferência de dados.
     */
    fun createPartner(partner: PartnerData): Mono<PartnerData>

    /**
     * Deleta um parceiro no banco de dados pelo Id. Retorna
     * [HttpStatus.NOT_FOUND] caso não seja encontrado nenhum parceiro.
     *
     * @exception ResponseStatusException
     * @param partner Id do parceiro.
     */
    fun deletePartnerById(partner: Long): Mono<Void>

    /**
     * Procura um parceiro pelo Id, retorna código [HttpStatus.NOT_FOUND]
     * caso não encontre nenhum parceiro.
     *
     * @exception ResponseStatusException
     * @see findPartnerByDocument
     */
    fun findPartnerById(id: Long): Mono<PartnerData>

    /**
     * Procura um parceiro pelo campo 'document', retorna código [HttpStatus.NOT_FOUND]
     * caso não encontre nenhum parceiro.
     *
     *
     * @param document Pode ser o CPF ou CNPJ do parceiro
     * @exception ResponseStatusException
     * @see findPartnerByDocument
     */
    fun findPartnerByDocument(document: String): Mono<PartnerData>

    /**
     * Procura os parceiros proximos de acordo com a [longitude] e [latitude],
     *
     * @param longitude longitude (x)
     * @param latitude longitude (y)
     * @param kilometers raio maximo em kilometros
     * - Se for definido como 0.0, apenas a area de cobertura ([Partner.coverageArea]) será considerada.
     *
     * @see findNearbyPartner
     */
    fun findNearbyPartners(longitude: Double, latitude: Double, kilometers: Double): Flux<PartnerData>

    /**
     * Procura o parceiro mais proximo de acordo com a [longitude] e [latitude],
     * retorna [HttpStatus.NOT_FOUND] caso não encontre nenhum parceiro.
     *
     * @param longitude longitude (x)
     * @param latitude longitude (y)
     * @param kilometers raio maximo em kilometros
     * - Se for definido como 0.0, apenas a area de cobertura ([Partner.coverageArea]) será considerada.
     *
     * @exception ResponseStatusException
     * @see findNearbyPartner
     */
    fun findNearbyPartner(longitude: Double, latitude: Double, kilometers: Double): Mono<PartnerData>
}