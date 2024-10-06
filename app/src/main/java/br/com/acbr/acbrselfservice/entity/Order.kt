package br.com.acbr.acbrselfservice.entity

import java.io.Serializable

data class Order (
    val id: String? = null,//"615c64e51257a04ae50f0712"
    val products: List<Product?>? = null
): Serializable
