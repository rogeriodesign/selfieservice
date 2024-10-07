package br.com.acbr.acbrselfservice.repository.store_data.service

import br.com.acbr.acbrselfservice.repository.RestGenericService
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource

class StoreDataService {


    suspend fun getDataSync(
        rote: String,
    ): Resource<StoreDataDto> {
        val service = RestGenericService().getStoreDataService(rote)
        val response = service.getStoreDataSync()
        return if (response.code() == 200 || response.code() == 201 || response.code() == 204) {
            Resource(response.body(), "", ProcessStatus.Success)
        } else {
            var message = response.code().toString()
            if (response.body() != null) {
                message += " - ${response.body()}"
            }
            if (response.message().isNotBlank()) {
                message += " - ${response.message()}"
            }
            if (response.errorBody() != null) {
                message += " - ${response.errorBody()}"
            }
            Resource(response.body(), message, ProcessStatus.Fail)
        }
    }
}