package me.sknz.zedelivery.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.model.geojson.MultiPolygon
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.PolygonCoordinates
import com.mongodb.client.model.geojson.Position
import me.sknz.zedelivery.dto.PartnerData
import me.sknz.zedelivery.utils.PartnerUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersUriSpec
import org.springframework.test.web.reactive.server.body
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class PartnerControllerTest {

    @Autowired
    lateinit var client: WebTestClient
    @Autowired
    lateinit var template: ReactiveMongoTemplate
    @BeforeEach
    fun setup() = PartnerUtils.populate(template)

    @Test
    @DisplayName("Listar os parceiros por paginação")
    fun listar_parceiros_por_paginacao() {
        client.get()
            .uri("/partner")
            .exchange()
                .expectStatus()
                .isOk()
    }

    @Test
    @DisplayName("Criar um novo parceiro")
    fun criar_um_parceiro() {
        val partner = getPartnerData()

        client.post().uri("/partner")
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(partner))
            .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .json(ObjectMapper().writeValueAsString(partner))
    }

    @Test
    @DisplayName("Erro ao tentar criar parceiro com id existente")
    fun erro_ao_criar_com_id_existente() {
        val partner = getPartnerData(1)

        client.post().uri("/partner")
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(partner))
            .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("message")
                .isEqualTo("Já existe um registro com este Id ${partner.id}")
    }

    @Test
    @DisplayName("Erro ao tentar criar parceiro com document existente")
    fun erro_ao_criar_com_document_existente() {
        val data = getPartnerData(417, "02.453.716/000170")

        client.post().uri("/partner")
            .body(Mono.just(data))
            .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("message")
                .isEqualTo("Já existe um registro com este documento ${data.document}")
    }

    @Test
    @DisplayName("Consultar um parceiro pelo Id")
    fun consultar_um_parceiro_pelo_id() {
        val id = 1
        client.get().uri("/partner/${id}")
            .exchange()
                .expectStatus()
                .isOk()
                .expectBody().jsonPath("id")
                .isEqualTo(id)
    }

    @Test
    @DisplayName("Consultar um parceiro pelo Document")
    fun consultar_um_parceiro_pelo_document() {
        val document = "02453716000170"
        client.get().uri("/partner/$document?type=document")
            .exchange()
                .expectStatus()
                .isOk()
                .expectBody().jsonPath("document")
                .isEqualTo(document)
    }

    @Test
    @DisplayName("Deletar um parceiro pelo Id")
    fun deletar_um_parceiro_pelo_id() {
        val id = 20
        client.delete().uri("/partner/$id")
            .exchange()
                .expectStatus()
                .isNoContent()
    }

    @Test
    @DisplayName("Erro ao tentar deletar usuário com Id Inexistente")
    fun erro_ao_deletar_usuario_id_inexistente() {
        val id = 54587
        client.delete().uri("/partner/$id")
            .exchange()
                .expectStatus()
                .isNotFound()
    }

    @Test
    @DisplayName("Procurar parceiro proximo")
    fun procurar_parceiro_proximo() {
        client.get().buildUri {
            path("/partner/nearby")
            queryParam("longitude", -46.76338)
            queryParam("latitude",-23.53597)
        }.exchange()
            .expectStatus()
            .isOk()
            .expectBody(PartnerData::class.java)
    }

    @Test
    @DisplayName("Procurar parceiro proximo por quilometros")
    fun procurar_parceiro_proximo_por_quilometro() {
        client.get().buildUri {
            path("/partner/nearby")
            queryParam("longitude", -46.76338)
            queryParam("latitude",-23.53597)
            queryParam("kilometers",6)
        }.exchange()
            .expectStatus()
            .isOk()
            .expectBody(PartnerData::class.java)
    }

    @Test
    @DisplayName("Erro ao tentar procurar parceiro inalcançavel")
    fun erro_procurar_parceiro_proximo_por_quilometro_inalcancavel() {
        client.get().buildUri {
            path("/partner/nearby")
            queryParam("longitude", -46.76338)
            queryParam("latitude",-23.53597)
            queryParam("kilometers",0.5)
        }.exchange()
            .expectStatus()
            .isNotFound()
    }

    @Test
    @DisplayName("Procurar todos os parceiros proximos")
    fun procurar_todos_os_parceiro_proximo() {
        client.get().buildUri {
            path("/partner/nearby")
            queryParam("longitude", -46.76338)
            queryParam("latitude",-23.53597)
            queryParam("type","all")
        }.exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(PartnerData::class.java)
            .hasSize(2)
    }

    @Test
    @DisplayName("Procurar todos os parceiros proximos por quilometro")
    fun procurar_todos_parceiros_proximo_por_quilometro() {
        client.get().buildUri {
            path("/partner/nearby")
            queryParam("longitude", -46.76338)
            queryParam("latitude",-23.53597)
            queryParam("kilometers",6)
            queryParam("type","all")
        }.exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(PartnerData::class.java)
            .hasSize(1)
    }

    private fun RequestHeadersUriSpec<*>.buildUri(init: UriBuilder.() -> Unit): WebTestClient.RequestHeadersSpec<*> {
        return this.uri { it.apply(init).build() }
    }

    private fun getPartnerData(id: Long = 52L, document: String = "12.345.678/000112"): PartnerData {
        val data = PartnerData()
        data.id = id
        data.ownerName = "Cristian Ferreira"
        data.tradingName = "Bar do SknZ"
        data.document = document
        data.address = Point(Position(-43.297337, -23.013538))
        data.coverageArea = getMultiPolygon()

        return data
    }

    private fun getMultiPolygon() = MultiPolygon(listOf(
        PolygonCoordinates(listOf(
            Position(30.0, 20.0),
            Position(45.0, 40.0),
            Position(40.0, 40.0),
            Position(30.0, 20.0),
        ))
    ))
}