package br.com.acbr.acbrselfservice.repository.order.service

import br.com.acbr.acbrselfservice.repository.PaginationDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface IOrderService {
    @GET("/v1/orders")
    suspend fun getOrdersSync(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int?,
        @Query("columns") columns: String?,//customer,merchants
        @Header("Authorization") authorization: String
    ): Response<PaginationDto<OrderDto>>

    @GET("/v1/order/{orderId}")
    suspend fun getOrderSync(
        @Path("orderId") orderId: String,
        @Header("Authorization") authorization: String
    ): Response<OrderDto>

    @DELETE("/v1/order/{orderId}")
    suspend fun deleteOrderSync(
        @Path("orderId") orderId: String,
        @Header("Authorization") authorization: String
    ): Response<ResponseBody>

    @POST("/v1/order")
    suspend fun postOrderSync(
        @Body body: NewOrderDto,
        @Header("Authorization") authorization: String
    ): Response<OrderDto>
}