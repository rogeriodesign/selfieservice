package br.com.acbr.acbrselfservice.repository.cart.service

import br.com.acbr.acbrselfservice.repository.ProductDto
import com.google.gson.annotations.SerializedName

data class CartDto (
    @SerializedName("total") val total: Double? = null,//20.56
    @SerializedName("products") val products: List<ProductDto?>? = null
)

