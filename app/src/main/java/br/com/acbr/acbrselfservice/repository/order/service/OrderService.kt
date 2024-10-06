package br.com.acbr.acbrselfservice.repository.order.service

import br.com.acbr.acbrselfservice.repository.PaginationDto
import br.com.acbr.acbrselfservice.repository.RestGenericService
import br.com.acbr.acbrselfservice.repository.cart.service.listProducts
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource
import okhttp3.ResponseBody

class OrderService {
    private val service = RestGenericService().getOrderService()

    suspend fun getOrdersSync(
        page: Int,
        perPage: Int?,
        columns: String?,
        authorization: String
    ): Resource<PaginationDto<OrderDto>> {
        try {
            val response = service.getOrdersSync(page, perPage, columns, authorization)
            return if (response.isSuccessful) {
                Resource(response.body(), "", ProcessStatus.Success)
            } else {
                var message = response.code().toString()
                if(response.body() != null){
                    message += " - ${response.body()}"
                }
                if(response.message().isNotBlank()){
                    message += " - ${response.message()}"
                }
                if(response.errorBody() != null){
                    message += " - ${response.errorBody()}"
                }
                Resource(response.body(), message, ProcessStatus.Fail)
            }
        } catch (e: Exception){
            return Resource(null, e.message?: e.toString(), ProcessStatus.Fail)
        }
    }

    suspend fun getOrderSync(
        orderId: String,
        authorization: String
    ): Resource<OrderDto> {
        try {
            val response = service.getOrderSync(orderId, authorization)
            return if (response.isSuccessful) {
                Resource(response.body(), "", ProcessStatus.Success)
            } else {
                var message = response.code().toString()
                if(response.body() != null){
                    message += " - ${response.body()}"
                }
                if(response.message().isNotBlank()){
                    message += " - ${response.message()}"
                }
                if(response.errorBody() != null){
                    message += " - ${response.errorBody()}"
                }
                Resource(response.body(), message, ProcessStatus.Fail)
            }
        } catch (e: Exception){
            return Resource(null, e.message?: e.toString(), ProcessStatus.Fail)
        }
    }

    suspend fun deleteOrderSync(
        orderId: String,
        authorization: String
    ): Resource<ResponseBody> {
        try {
            val response = service.deleteOrderSync(orderId, authorization)
            return if (response.isSuccessful) {
                Resource(response.body(), "", ProcessStatus.Success)
            } else {
                var message = response.code().toString()
                if(response.body() != null){
                    message += " - ${response.body()}"
                }
                if(response.message().isNotBlank()){
                    message += " - ${response.message()}"
                }
                if(response.errorBody() != null){
                    message += " - ${response.errorBody()}"
                }
                Resource(response.body(), message, ProcessStatus.Fail)
            }
        } catch (e: Exception){
            return Resource(null, e.message?: e.toString(), ProcessStatus.Fail)
        }
    }

    suspend fun postOrderSync(
        order: NewOrderDto,
        authorization: String
    ): Resource<OrderDto> {
//        try {
//            val service = RestGenericService(5).getOrderService()
//            val response = service.postOrderSync(order, authorization)
//            if (response.raw().networkResponse != null) {
//                Log.i("cache",
//                    "onResponse: response is from NETWORK..."
//                )
//            } else if (response.raw().cacheResponse != null
//                && response.raw().networkResponse == null
//            ) {
//                Log.i("cache",
//                    "onResponse: response is from CACHE..."
//                )
//            }
//            return if (response.isSuccessful) {
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
        return Resource(
            OrderDto(
                id = "123456789",
                products = listProducts
            ),
            "", ProcessStatus.Success)
    }
}

