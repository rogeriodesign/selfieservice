package br.com.acbr.acbrselfservice.entity

data class Credentials (
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val refreshExpiresIn: Long,
    val tokenType: String,
    val deviceUniqueId: String
)