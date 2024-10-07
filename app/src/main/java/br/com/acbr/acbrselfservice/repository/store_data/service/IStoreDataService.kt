package br.com.acbr.acbrselfservice.repository.store_data.service

import retrofit2.Response
import retrofit2.http.POST

interface IStoreDataService {

    @POST("/v1/auth/refresh-token")
    suspend fun getStoreDataSync(
    ): Response<StoreDataDto>
}