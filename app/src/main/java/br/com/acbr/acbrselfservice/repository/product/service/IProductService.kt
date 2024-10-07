package br.com.acbr.acbrselfservice.repository.product.service

import br.com.acbr.acbrselfservice.repository.PaginationDto
import br.com.acbr.acbrselfservice.repository.PriceDto
import br.com.acbr.acbrselfservice.repository.ProductDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface IProductService {
    @GET("/v1/merchants/{merchantId}/categories")
    fun getCategories(
        @Path("merchantId") merchantId : String,
        @Header("Authorization") authorization: String
    ): Call<PaginationDto<CategoryDto>>

    @GET("/v1/merchants/{merchantId}/products")
    fun searchProducts(
        @Path("merchantId") merchantId: String,
        @Query("search") search: String,
        @Query("page") page: Int,
        @Header("Authorization") authorization: String
    ): Call<PaginationDto<ProductDto>>

    @GET("/v1/merchants/{merchantId}/products/category/{categoryId}")
    fun getProductsByCategory(
        @Path("merchantId") merchantId: String,
        @Path("categoryId") categoryId: String,
        @Query("page") page: Int,
        @Header("Authorization") authorization: String
    ): Call<PaginationDto<ProductDto>>

    @GET("/v1/merchants/{merchantId}/products/{productId}/price")
    fun getProductPrice(
        @Path("merchantId") merchantId: String,
        @Path("productId") productId: String,
        @Header("Authorization") authorization: String
    ): Call<PriceDto>

    @GET("/v1/merchants/{merchantId}/products")
    suspend fun getProductsSync(
        @Query("search") search: String?,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): Response<PaginationDto<ProductDto>>

    @GET("/v1/merchants/{merchantId}/products/sort-by-category")
    suspend fun getProductsByCategorySync(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): Response<PaginationDto<ProductDto>>

    @GET("/v1/merchants/{merchantId}/products/{productId}")
    suspend fun getProductSync(
        @Path("merchantId") merchantId: String,
        @Path("productId") productId: String,
        @Header("Authorization") authorization: String
    ): Response<ProductDto>
}