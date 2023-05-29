package me.sknz.zedelivery.dto.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.hibernate.validator.constraints.br.CPF
import org.hibernate.validator.constraints.br.CNPJ

/**
 * ## CPFouCNPJValidator
 *
 * ConstraintValidator para a anotação [CPFouCNPJ] incoporando o pattern
 * das anotações [CPF] e [CNPJ].
 *
 * Caso o valor não seja um CPF ou CNPJ, a função [isValid] irá retornar false.
 */
object CPFouCNPJValidator: ConstraintValidator<CPFouCNPJ, String> {

    private const val CPF_REGEX = "([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})|([0-9]{11})"
    private const val CNPJ_REGEX = "([0-9]{2}[.]?[0-9]{3}[.]?[0-9]{3}[/]?[0-9]{4}[-]?[0-9]{2})"

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        return isValidCPF(value) || isValidCNPJ(value)
    }


    fun isValidCPF(value: String): Boolean {
        return value.matches(Regex(CPF_REGEX))
    }

    fun isValidCNPJ(value: String): Boolean {
        return value.matches(Regex(CNPJ_REGEX))
    }
}
