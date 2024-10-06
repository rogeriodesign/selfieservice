package br.com.acbr.acbrselfservice.util

data class Resource<T>(
    val data: T?,
    val message: String,
    val status: ProcessStatus
)