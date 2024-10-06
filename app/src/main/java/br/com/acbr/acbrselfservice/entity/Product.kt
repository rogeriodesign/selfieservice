package br.com.acbr.acbrselfservice.entity

import java.io.Serializable



data class Product (
    val id: Long, //123456
    val uuid: String,//"d184fd56-1f9d-42f0-b4b1-91a3cb27b11c"
    val name: String?,//"Sanduíche"
    val description: String?,//"Descrição do item..."
    val externalCode: String?,//"c01-i001"
    val imagePath: String?,//"path/path"
    val index: String?,//"0"
    val sequence: String?,//"0"
    val serving: String?,//"SERVES_1"
    val status: String?,//"AVAILABLE"
    val originalValue: Double?,//20
    val value: Double?,
    val amount: Double?,
    val optionGroups: List<ProductOptionGroup?>?,
    val category: Category? = null,
    val notes: String? = null
): Serializable
