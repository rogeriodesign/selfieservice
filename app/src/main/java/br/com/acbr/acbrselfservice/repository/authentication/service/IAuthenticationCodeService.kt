package br.com.acbr.acbrselfservice.repository.authentication.service

import br.com.acbr.acbrselfservice.repository.TokenResponseDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthenticationCodeService {
    @POST("/v1/auth/email")
    fun generateVerificationCodeEmail(
        @Body body: GenerateVerificationCodeRequestEmailDto
    ): Call<ResponseBody>

    @POST("/v1/auth/email/code")
    fun verificationCodeEmail(
        @Body body: VerificationCodeRequestEmailDto
    ): Call<TokenResponseDto>

    @POST("/v1/auth/phone-number")
    fun generateVerificationCodePhone(
        @Body body: GenerateVerificationCodeRequestPhoneDto
    ): Call<ResponseBody>

    @POST("/v1/auth/phone-number/code")
    fun verificationCodePhone(
        @Body body: VerificationCodeRequestPhoneDto
    ): Call<TokenResponseDto>

    @POST("/v1/auth/refresh-token")
    fun refreshToken(
        @Body body: RefreshTokenRequestDto
    ): Call<TokenResponseDto>

    @POST("/v1/auth/refresh-token")
    suspend fun refreshTokenSync(
        @Body body: RefreshTokenRequestDto
    ): Response<TokenResponseDto>
}