package br.com.acbr.acbrselfservice.entity

data class Token (
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val refreshExpiresIn: Long,
    val tokenType: String //"Bearer"
)