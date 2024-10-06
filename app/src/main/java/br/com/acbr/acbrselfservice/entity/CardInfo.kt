package br.com.acbr.acbrselfservice.entity

data class CardInfo (
    val holderName: String,
    val number: String,
    val expirationDate: String,
    val cvv: String,
    var token: String?
)
