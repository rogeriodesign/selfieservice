package br.com.acbr.acbrselfservice.repository.profile.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface IMeService {
    @GET("/v1/me")
    fun getProfileData(
        @Header("Authorization") authorization: String
    ): Call<ProfileResponseDto>

    @PUT("/v1/me")
    fun nameUpdate(
        @Body name: NameRequestDto,
        @Header("Authorization") authorization: String
    ): Call<ProfileResponseDto>

    @PATCH("/v1/me/email")
    fun emailUpdate(
        @Body emailRequest: EmailRequestDto,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @POST("/v1/me/email/code")
    fun validateEmailUpdate(
        @Body emailCodeRequest: EmailCodeRequestDto,
        @Header("Authorization") authorization: String
    ): Call<ProfileResponseDto>

    @PATCH("/v1/me/phone-number")
    fun phoneNumberUpdate(
        @Body phoneRequest: PhoneRequestDto,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @POST("/v1/me/phone-number/code")
    fun validatePhoneNumberUpdate(
        @Body phoneCodeRequest: PhoneCodeRequestDto,
        @Header("Authorization") authorization: String
    ): Call<ProfileResponseDto>
}