package me.sknz.zedelivery.dto.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload

import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CPFouCNPJValidator::class])
annotation class CPFouCNPJ(
    val message: String = "CPF ou CNPJ inválido",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)