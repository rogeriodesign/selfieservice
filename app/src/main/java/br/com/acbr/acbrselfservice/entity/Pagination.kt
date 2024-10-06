package br.com.acbr.acbrselfservice.entity

data class Pagination<T> (
    val currentPage: Int,
    val data: List<T>,
    val lastPage: Int?,
    val perPage: Int?,
    val total: Int?
)