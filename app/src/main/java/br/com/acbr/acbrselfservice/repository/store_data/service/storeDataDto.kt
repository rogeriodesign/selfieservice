package br.com.acbr.acbrselfservice.repository.store_data.service

import com.google.gson.annotations.SerializedName

data class StoreDataDto (
    @SerializedName("logo") val logo: String,
    @SerializedName("head_image") val headImage: String,
    @SerializedName("ticket") val ticket: Boolean
)


