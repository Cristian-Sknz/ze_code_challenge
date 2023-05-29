package me.sknz.zedelivery.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer


@EnableWebFlux
@Configuration
class WebFluxConfiguration: WebFluxConfigurer {

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(ReactivePageableHandlerMethodArgumentResolver())
    }
}