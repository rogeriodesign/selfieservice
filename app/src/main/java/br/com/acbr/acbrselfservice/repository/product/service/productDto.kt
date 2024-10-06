package br.com.acbr.acbrselfservice.repository.product.service

import com.google.gson.annotations.SerializedName

data class CategoryDto (
    @SerializedName("uuid") val uuid: String,//"2f862147-33ae-476d-b313-772eebd36b54"
    @SerializedName("name") val name: String,//"Nome da categoria"
    @SerializedName("status") val status: String,//"AVAILABLE"
    @SerializedName("template") val template: String//"PIZZA"
)


