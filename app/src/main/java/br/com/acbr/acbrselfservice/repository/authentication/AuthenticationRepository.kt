package br.com.acbr.acbrselfservice.repository.authentication

import br.com.acbr.acbrselfservice.entity.Token
import br.com.acbr.acbrselfservice.repository.authentication.service.*
import br.com.acbr.acbrselfservice.util.Resource

class AuthenticationRepository(private val service: AuthenticationService) {

    fun generateCodeEmail(
        email: String,
        deviceUniqueId: String,
        onPostExecute: (Resource<String>) -> Unit
    ){
        service.generateVerificationCodeEmail(
            GenerateVerificationCodeRequestEmailDto(
            email, deviceUniqueId
        )
        ){
            val data = if(it.data != null){
                it.data.toString()
            } else {
                null
            }
            onPostExecute(Resource(data, it. message, it.status))
        }
    }

    fun verificationCodeEmail(
        email: String,
        code: String,
        deviceUniqueId: String,
        onPostExecute: (Resource<Token>) -> Unit
    ){
        service.verificationCodeEmail(
            VerificationCodeRequestEmailDto(
            email, code, deviceUniqueId
        )
        ){
            val data = if(it.data != null){
                Token(
                    accessToken = it.data.accessToken,
                    refreshToken = it.data.refreshToken,
                    expiresIn = it.data.expiresIn,
                    refreshExpiresIn = it.data.refreshExpiresIn,
                    tokenType = it.data.tokenType
                )
            } else {
                null
            }
            onPostExecute(Resource(data, it. message, it.status))
        }
    }

    fun generateCodePhone(
        phoneNumber: String,
        deviceUniqueId: String,
        onPostExecute: (Resource<String>) -> Unit
    ){
        service.generateVerificationCodePhone(
            GenerateVerificationCodeRequestPhoneDto(
            phoneNumber, deviceUniqueId
        )
        ){
            val data = if(it.data != null){
                it.data.toString()
            } else {
                null
            }
            onPostExecute(Resource(data, it. message, it.status))
        }
    }

    fun verificationCodePhone(
        phoneNumber: String,
        code: String,
        deviceUniqueId: String,
        onPostExecute: (Resource<Token>) -> Unit
    ){
        service.verificationCodePhone(
            VerificationCodeRequestPhoneDto(
                phoneNumber, code, deviceUniqueId
            )
        ){
            val data = if(it.data != null){
                Token(
                    accessToken = it.data.accessToken,
                    refreshToken = it.data.refreshToken,
                    expiresIn = it.data.expiresIn,
                    refreshExpiresIn = it.data.refreshExpiresIn,
                    tokenType = it.data.tokenType
                )
            } else {
                null
            }
            onPostExecute(Resource(data, it. message, it.status))
        }
    }

    fun refreshToken(
        refreshToken: String,
        onPostExecute: (Resource<Token>) -> Unit
    ){
        service.refreshToken(
            RefreshTokenRequestDto(refreshToken)
        ){
            val data = if(it.data != null){
                Token(
                    accessToken = it.data.accessToken,
                    refreshToken = it.data.refreshToken,
                    expiresIn = it.data.expiresIn,
                    refreshExpiresIn = it.data.refreshExpiresIn,
                    tokenType = it.data.tokenType
                )
            } else {
                null
            }
            onPostExecute(Resource(data, it. message, it.status))
        }
    }

    suspend fun refreshTokenSync(
        refreshToken: String
    ): Resource<Token> {
        val response = service.refreshTokenSync(
            RefreshTokenRequestDto(refreshToken)
        )
        val data = if(response.data != null){
            Token(
                accessToken = response.data.accessToken,
                refreshToken = response.data.refreshToken,
                expiresIn = response.data.expiresIn,
                refreshExpiresIn = response.data.refreshExpiresIn,
                tokenType = response.data.tokenType
            )
        } else {
            null
        }
        return Resource(data, response. message, response.status)
    }
}