package br.com.acbr.acbrselfservice.repository.profile.service

import com.google.gson.annotations.SerializedName
import java.util.*

data class ProfileResponseDto (
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("login_id") val loginId: String? = null,
    @SerializedName("updated_at") val updatedAt: Date? = null,
    @SerializedName("created_at") val createdAt: Date? = null,
    @SerializedName("addresses") val addresses: List<List<AddressDto>?>? = null
)

data class AddressDto (
    @SerializedName("_id") val id: String? = null,
    @SerializedName("street") val street: String? = null,
    @SerializedName("number") val number: String? = null,
    @SerializedName("complement") val complement: String? = null,
    @SerializedName("postal_code") val postalCode: String? = null,
    @SerializedName("neighborhood") val neighborhood: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("reference_point") val referencePoint: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("customer") val customer: String? = null
)

data class NameRequestDto (
    @SerializedName("name") val name: String
)

data class EmailRequestDto (
    @SerializedName("email") val email: String,
    @SerializedName("device_unique_id") val deviceUniqueId: String
)

data class EmailCodeRequestDto (
    @SerializedName("email") val email: String,
    @SerializedName("device_unique_id") val deviceUniqueId: String,
    @SerializedName("code") val code: String
)

data class PhoneRequestDto (
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("device_unique_id") val deviceUniqueId: String
)

data class PhoneCodeRequestDto (
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("device_unique_id") val deviceUniqueId: String,
    @SerializedName("code") val code: String
)
