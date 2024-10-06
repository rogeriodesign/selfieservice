package br.com.acbr.acbrselfservice.repository.authentication.service

import com.google.gson.annotations.SerializedName

data class GenerateVerificationCodeRequestEmailDto (
    @SerializedName("email") val email: String,
    @SerializedName("device_unique_id") val deviceUniqueId: String
)

data class VerificationCodeRequestEmailDto (
    @SerializedName("email") val email: String,
    @SerializedName("code") var code: String,
    @SerializedName("device_unique_id") var deviceUniqueId: String
)

data class GenerateVerificationCodeRequestPhoneDto (
    @SerializedName("phone_number") var phoneNumber: String,
    @SerializedName("device_unique_id") var deviceUniqueId: String
)

data class VerificationCodeRequestPhoneDto (
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("code") var code: String,
    @SerializedName("device_unique_id") var deviceUniqueId: String
)

data class RefreshTokenRequestDto (
    @SerializedName("refresh_token") val refreshToken: String
)
