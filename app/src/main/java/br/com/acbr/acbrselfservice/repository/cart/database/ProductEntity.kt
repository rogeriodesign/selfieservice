package br.com.acbr.acbrselfservice.repository.cart.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uuid: String,//"d184fd56-1f9d-42f0-b4b1-91a3cb27b11c"**
    val name: String?,//"Sanduíche"**
    val description: String?,//"Descrição do item..."**
    val externalCode: String?,//"c01-i001"**
    val imagePath: String?,//"path/path"**
    val index: String?,//"0"**
    val sequence: String?,//"0"**
    val serving: String?,//"SERVES_1" "NOT_APPLICABLE"**
    val status: String?,//"AVAILABLE"**
    val originalValue: Double?,//20
    val value: Double?,
    val amount: Double?,
    val categoryUuid: String? = null,
    val notes: String? = null
)

