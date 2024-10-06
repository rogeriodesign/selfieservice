package br.com.acbr.acbrselfservice.entity

import java.io.Serializable

data class Category(
    val uuid: String,
    val name: String,//"Nome da categoria"
    val status: String? = null,//"AVAILABLE"
    val template: String? = null//"PIZZA"
): Serializable
