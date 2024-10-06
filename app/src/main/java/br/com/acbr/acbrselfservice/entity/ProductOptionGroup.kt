package br.com.acbr.acbrselfservice.entity

import java.io.Serializable

data class ProductOptionGroup (
    val id: String?,//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
    val index: Int?,//0
    val maximum: Int?,//2
    val minimum: Int?,//0
    val name: String?,//"Adicionais"
    val options: List<ProductOption?>?
): Serializable{
    override fun toString(): String {
        return "ProductOptionGroup(id=$id, index=$index, maximum=$maximum, minimum=$minimum, name=$name, options=$options)"
    }
}
