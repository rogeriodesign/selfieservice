package br.com.acbr.acbrselfservice.entity

import br.com.acbr.acbrselfservice.repository.OptionGroupDto
import java.io.Serializable



data class ProductOption (
    val id: Long,
    val uuid: String,
    val productId: Long? = null,
    val description: String? = null,//"molho extra"
    val name: String,//"Bacon"
    val originalValue: Double? = null,
    val value: Double? = null,
    val sequence: Int? = null,
    val status: String? = null,
    val imagePath: String? = null,
    val amount: Double? = null,
    val index: Int? = null,
    val optionGroups: List<OptionGroupDto>? = null
): Serializable {
    override fun toString(): String {
        return "ProductOption(name='$name', description=$description, imagePath=$imagePath, amount=$amount, optionGroups=$optionGroups)"
    }
}
