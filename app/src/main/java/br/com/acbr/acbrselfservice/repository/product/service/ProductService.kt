package br.com.acbr.acbrselfservice.repository.product.service

import br.com.acbr.acbrselfservice.repository.*
import br.com.acbr.acbrselfservice.repository.cart.service.listCategory
import br.com.acbr.acbrselfservice.repository.cart.service.listProducts
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource

class ProductService {
    val service = RestGenericService().getProductService()

    fun getCategories(
        onPostExecute: (Resource<PaginationDto<CategoryDto>>) -> Unit
    ){
//        service.getCategories(merchantId,
//            authorization).enqueue(
//            GenericCallback<PaginationDto<CategoryDto>>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterdado
        onPostExecute(Resource(
            PaginationDto(
                1,
                listCategory,
                1,
                10,
                2
            ), "", ProcessStatus.Success))
    }



    fun searchProducts(
        search: String,
        page: Int,
        onPostExecute: (Resource<PaginationDto<ProductDto>>) -> Unit
    ){
//        service.searchProducts(merchantId, search, page, authorization).enqueue(
//            GenericCallback<PaginationDto<ProductDto>>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterado
        onPostExecute(Resource(
            PaginationDto(
                1,
                listProducts,
                1,
                10,
                2
            ), "", ProcessStatus.Success))
    }

    fun getProductsByCategory(
        categoryId: String,
        page: Int,
        onPostExecute: (Resource<PaginationDto<ProductDto>>) -> Unit
    ){
//        service.getProductsByCategory(merchantId, categoryId, page, authorization).enqueue(
//            GenericCallback<PaginationDto<ProductDto>>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterado
        onPostExecute(Resource(
            PaginationDto(
                1,
                listProducts,
                1,
                10,
                2
            ), "", ProcessStatus.Success))
    }

    fun getProductPrice(
        productId: String,
        onPostExecute: (Resource<PriceDto>) -> Unit
    ){
//        service.getProductPrice(merchantId, productId, authorization).enqueue(
//            GenericCallback<PriceDto>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterado
        onPostExecute(Resource(
            PriceDto(
                originalValue = 20.0,
                value = 19.8
            ), "", ProcessStatus.Success))
    }

    suspend fun getProductsSync(
        search: String?,
        page: Int,
        perPage: Int,
    ): Resource<PaginationDto<ProductDto>>{
//        try {
//            val response = service.getProductsSync(
//                search,
//                page,
//                perPage
//            )
//            return if (response.code() == 200) {//response.isSuccessful
//                Resource(response.body(), "", ProcessStatus.Success)
//            } else {
//                var message = response.code().toString()
//                if(response.body() != null){
//                    message += " - ${response.body()}"
//                }
//                if(response.message().isNotBlank()){
//                    message += " - ${response.message()}"
//                }
//                if(response.errorBody() != null){
//                    message += " - ${response.errorBody()}"
//                }
//                Resource(response.body(), message, ProcessStatus.Fail)
//            }
//        } catch (e: Exception){
//            return Resource(null, e.message?: e.toString(), ProcessStatus.Fail)
//        }
        // alterado
        return Resource(
            PaginationDto(
                1,
                listProducts,
                1,
                10,
                2
            ), "", ProcessStatus.Success)
    }

    suspend fun getProductsByCategorySync(
        page: Int,
        perPage: Int,
    ): Resource<PaginationDto<ProductDto>>{
//        try {
//            val response = service.getProductsByCategorySync(
//                page,
//                perPage
//            )
//            return if (response.code() == 200) {//response.isSuccessful
//                Resource(response.body(), "", ProcessStatus.Success)
//            } else {
//                var message = response.code().toString()
//                if(response.body() != null){
//                    message += " - ${response.body()}"
//                }
//                if(response.message().isNotBlank()){
//                    message += " - ${response.message()}"
//                }
//                if(response.errorBody() != null){
//                    message += " - ${response.errorBody()}"
//                }
//                Resource(response.body(), message, ProcessStatus.Fail)
//            }
//        } catch (e: Exception){
//            return Resource(null, e.message?: e.toString(), ProcessStatus.Fail)
//        }
        // alterado
        return Resource(
            PaginationDto(
                1,
                listProducts,
                1,
                10,
                2
            ), "", ProcessStatus.Success)
    }

    suspend fun getProductSync(
        productId: String
    ): Resource<ProductDto>{
//        try {
//            val response = service.getProductSync(
//                merchantId,
//                productId,
//                authorization
//            )
//            return if (response.code() == 200) {//response.isSuccessful
//                Resource(response.body(), "", ProcessStatus.Success)
//            } else {
//                var message = response.code().toString()
//                if(response.body() != null){
//                    message += " - ${response.body()}"
//                }
//                if(response.message().isNotBlank()){
//                    message += " - ${response.message()}"
//                }
//                if(response.errorBody() != null){
//                    message += " - ${response.errorBody()}"
//                }
//                Resource(response.body(), message, ProcessStatus.Fail)
//            }
//        } catch (e: Exception){
//            return Resource(null, e.message?: e.toString(), ProcessStatus.Fail)
//        }
        // alterado
        return Resource(
            listProducts[0], "", ProcessStatus.Success)
    }

}