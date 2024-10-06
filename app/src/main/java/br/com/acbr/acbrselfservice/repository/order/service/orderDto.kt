package br.com.acbr.acbrselfservice.repository.order.service

import br.com.acbr.acbrselfservice.repository.ProductDto
import com.google.gson.annotations.SerializedName

data class OrderDto (
    @SerializedName("_id") val id: String? = null,
    @SerializedName("products") val products: List<ProductDto?>? = null
)

data class NewOrderDto (
    @SerializedName("cart_id") val cartId: String,//4L01s23f4S45656dD
    @SerializedName("payment") val payment: String,//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJob2xkZXJfbmFtZSI6IkN1c3RvbWVyIFRlc3RlIiwibnVtYmVyIjoiNDU1NjM2Njk0MTA2MjEyMiIsImV4cGlyYXRpb25fZGF0ZSI6IjEyMjMiLCJjdnYiOiIxMTEifQ.LBnlvk5WUam4QO8FefyU2_vkB61aPqRQB6COsIJdsX0
    @SerializedName("delivery") val delivery: String//TAKE_AWAY
)

data class CustomerDto (
    @SerializedName("_id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null
)
