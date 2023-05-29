<div align="center">
  <div>
    <img width="190px" src=".github/Ze_logo.png" alt="logo"/>
    <h1 align="center">Zé Code Challenge</h1>
  </div>
	<h2><a href="https://github.com/ZXVentures/ze-code-challenges/blob/master/backend_pt.md">Desafio Backend Zé Delivery</a></h2>
	<p>Um serviço que implementa funcionalidades de Geo Localização e gerenciamento de parceiros.</p>
</div>

---

## Projeto

Este é um projeto que implementa o serviço e as funcionalidades descritas no [Desafio de backend do Zé](https://github.com/ZXVentures/ze-code-challenges/blob/master/backend_pt.md), utilizando Spring Webflux e Reactive MongoDB, incluindo testes de integração.

> Mais adiante, falarei sobre como este projeto foi desenvolvido.

### Rotas da aplicação

Os testes foram feitos com os [dados gerados pela equipe do Zé](https://github.com/ZXVentures/ze-code-challenges/blob/master/files/pdvs.json). Ao iniciar a aplicação, o banco de dados não estará populado.

| Método | Rota                | Descrição                                                                                                     |
|--------|---------------------|---------------------------------------------------------------------------------------------------------------|
| GET    | /partner            | Retorna todos os parceiros cadastrados (com paginação)                                                        |
| POST   | /partner            | Cria um novo parceiro de acordo com os dados passados                                                         |
| GET    | /partner/{id}       | Retorna um parceiro pelo ID (numérico)                                                                        |
| GET    | /partner/{document} | Retorna um parceiro pelo documento (CPF ou CNPJ)                                                              |
| DELETE | /partner/{id}       | Deleta um parceiro pelo ID (numérico)                                                                         |
| GET    | /partner/nearby     | Retorna o(s) parceiro(s) mais próximo(s) da localização solicitada pelos parâmetros `longitude` e `latitude` |


Decidi aprimorar as rotas solicitadas no desafio, adicionando parâmetros para uma busca mais personalizável e flexível.

| Rota            | Parâmetro   | Descrição                                                                                                                                                |
|-----------------|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| /partner/{id}   | ?type       | Por padrão, definido como ID. Este parâmetro sinaliza se a pesquisa será por ID ou Documento (`type=document`).                                          |
| /partner/nearby | ?type       | Por padrão, não é definido. Este parâmetro sinaliza qual o tipo de dados o cliente deseja: apenas um parceiro (`type=?`) ou todos ao redor (`type=all`). |
| /partner/nearby | ?kilometers | Por padrão, não é definido. Este parâmetro de pesquisa serve para filtrar parceiros em um raio por quilômetros.                                          |

## Iniciar o ambiente

Para poder executar o ambiente inteiro, você precisará atender a alguns requisitos:
* Java 17+
* Gradle 7.6.1 ou mais recente
* Docker
* Docker-compose

Após atender aos requisitos, será necessário fazer o build da aplicação com o Gradle, apenas execute o comando `gradlew build` na raiz do projeto.

Com o build realizado, só resta executar o projeto com o docker-compose, usando o comando `docker-compose up -d`.

A aplicação está configurada corretamente e "pronta" para produção, sem necessidade de configurações adicionais.

---

## Conclusão

Este projeto foi relativamente tranquilo de ser feito. No processo, aprendi como lidar com GeoJSON no Spring Data MongoDB e descobri que o MongoDB tem uma ótima compatibilidade com geometria espacial. Decidi usar o MongoDB porque ele é um ótimo banco não relacional para armazenar dados complexos e ainda manter um bom desempenho.

Houve uma dificuldade ao fazer os testes

 de integração, pois tive que desenvolver uma forma de rodar o MongoDB de forma embutida e reativa nos testes. Utilizei a biblioteca `de.flapdoodle.embed.mongo`, porém tive que tentar várias formas de implementar a conexão com os testes. Consegui configurar e está funcionando corretamente.

Tive que criar alguns serializadores e deserializadores para o Jackson, porque queria que a resposta das requisições HTTP fosse idêntica ao exemplo dado no desafio.

Outros detalhes do desafio que não deixei passar foram a questão do campo `document`, que foi exigido ser um campo único. Nos dados disponibilizados, havia inconsistências como documentos com máscara (tinha CNPJ/CPF com e sem máscara). Decidi armazenar esses campos sem máscara em formato de string.

Deixarei alguns links de sites que pesquisei sobre GeoJSON que foram úteis no processo de desenvolvimento desta aplicação.

#### Erro em testes
Caso ocorra um erro nos testes, pode ser por causa do Embedded Mongo. Verifique se há um processo com o nome `mongod` e finalize-o.

### Referências
* [GeoJSON (wikipedia)](https://en.wikipedia.org/wiki/GeoJSON)
* [MongoDB GeoSpatial Support (baeldung)](https://baeldung.com/mongodb-geospatial-support)
* [MongoDB GeoSpatial (spring reference)](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.geospatial)
* [Flapdoodle Embed Mongo (github)](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)
