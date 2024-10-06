package br.com.acbr.acbrselfservice.repository.cart.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductOptionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uuid: String,
    val productId: Long,
    val description: String? = null,//"molho extra"
    val name: String,//"Bacon"
    val originalValue: Double? = null,
    val value: Double? = null,
    val sequence: Int? = null,
    val status: String? = null,
    val imagePath: String? = null,
    val amount: Double? = null,
    val index: Int? = null
)

