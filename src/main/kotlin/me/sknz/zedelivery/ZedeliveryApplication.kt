package me.sknz.zedelivery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
class ZedeliveryApplication

fun main(args: Array<String>) {
	runApplication<ZedeliveryApplication>(*args)
}