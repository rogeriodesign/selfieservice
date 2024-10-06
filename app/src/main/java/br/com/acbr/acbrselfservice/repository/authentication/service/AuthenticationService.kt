package br.com.acbr.acbrselfservice.repository.authentication.service

import br.com.acbr.acbrselfservice.repository.TokenResponseDto
import br.com.acbr.acbrselfservice.repository.GenericCallback
import br.com.acbr.acbrselfservice.repository.RestGenericService
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource
import okhttp3.ResponseBody

class AuthenticationService {
    fun generateVerificationCodeEmail(
        request: GenerateVerificationCodeRequestEmailDto,
        onPostExecute: (Resource<ResponseBody>) -> Unit
    ){
        val service = RestGenericService().getAuthenticationService()
        service.generateVerificationCodeEmail(request).enqueue(
            GenericCallback<ResponseBody>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun verificationCodeEmail(
        request: VerificationCodeRequestEmailDto,
        onPostExecute: (Resource<TokenResponseDto>) -> Unit
    ){
        val service = RestGenericService().getAuthenticationService()
        service.verificationCodeEmail(request).enqueue(
            GenericCallback<TokenResponseDto>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun generateVerificationCodePhone(
        request: GenerateVerificationCodeRequestPhoneDto,
        onPostExecute: (Resource<ResponseBody>) -> Unit
    ){
        val service = RestGenericService().getAuthenticationService()
        service.generateVerificationCodePhone(request).enqueue(
            GenericCallback<ResponseBody>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun verificationCodePhone(
        request: VerificationCodeRequestPhoneDto,
        onPostExecute: (Resource<TokenResponseDto>) -> Unit
    ){
        val service = RestGenericService().getAuthenticationService()
        service.verificationCodePhone(request).enqueue(
            GenericCallback<TokenResponseDto>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun refreshToken(
        request: RefreshTokenRequestDto,
        onPostExecute: (Resource<TokenResponseDto>) -> Unit
    ){
        val service = RestGenericService().getAuthenticationService()
        service.refreshToken(request).enqueue(
            GenericCallback<TokenResponseDto>(
                onPostExecute = onPostExecute
            )
        )
    }

    suspend fun refreshTokenSync(
        request: RefreshTokenRequestDto
    ): Resource<TokenResponseDto> {
        val service = RestGenericService().getAuthenticationService()
        val response = service.refreshTokenSync(request)
        return if (response.code() == 200 || response.code() == 201 || response.code() == 204) {
            Resource(response.body(), "", ProcessStatus.Success)
        } else {
            var message = response.code().toString()
            if(response.body() != null){
                message += " - ${response.body()}"
            }
            if(response.message().isNotBlank()){
                message += " - ${response.message()}"
            }
            if(response.errorBody() != null){
                message += " - ${response.errorBody()}"
            }
            Resource(response.body(), message, ProcessStatus.Fail)
        }
    }
}