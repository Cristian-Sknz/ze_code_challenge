package me.sknz.zedelivery

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.transitions.Mongod
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess
import de.flapdoodle.reverse.transitions.Start
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmbeddedMongo {

    @Bean(destroyMethod = "stop")
    fun mongod(): RunningMongodProcess {
        val mongod = Mongod.instance()
            .withNet(Start.to(Net::class.java)
                .initializedWith(Net.of("localhost", 27018, true)))
            .start(Version.V6_0_3)

        return mongod.current()
    }

    @Bean
    fun mongoClient(process: RunningMongodProcess): MongoClient {
        return MongoClients.create("mongodb://${process.serverAddress.host}:${process.serverAddress.port}/test")
    }
}
