package br.com.acbr.acbrselfservice.repository.cart.service

import br.com.acbr.acbrselfservice.repository.ProductDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ICartService {
    @GET("/v1/cart/{deviceUniqueId}")
    fun getCart(
        @Path("deviceUniqueId") deviceUniqueId: String,
        @Header("Authorization") authorization: String
    ): Call<CartDto>

    @DELETE("/v1/cart/{deviceUniqueId}")
    fun delete(
        @Path("deviceUniqueId") deviceUniqueId: String,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @POST("/v1/cart")
    fun postCart(
        @Body request: CartDto,
        @Header("Authorization") authorization: String
    ): Call<CartDto>

    @POST("/v1/cart/{deviceUniqueId}/{merchantId}/product")
    fun postProduct(
        @Path("deviceUniqueId") deviceUniqueId: String,
        @Path("merchantId") merchantId: String,
        @Body request: ProductDto,
        @Header("Authorization") authorization: String
    ): Call<CartDto>

    @PUT("/v1/cart/{deviceUniqueId}/{merchantId}/product/{index}")
    fun putProduct(
        @Path("deviceUniqueId") deviceUniqueId: String,
        @Path("merchantId") merchantId: String,
        @Path("index") index: Int,
        @Body request: ProductDto,
        @Header("Authorization") authorization: String
    ): Call<CartDto>

    @DELETE("/v1/cart/{deviceUniqueId}/{merchantId}/product/{index}")
    fun deleteProduct(
        @Path("deviceUniqueId") deviceUniqueId: String,
        @Path("merchantId") merchantId: String,
        @Path("index") index: Int,
        @Header("Authorization") authorization: String
    ): Call<CartDto>
}