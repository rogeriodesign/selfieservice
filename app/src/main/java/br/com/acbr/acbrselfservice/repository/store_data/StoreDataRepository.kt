package br.com.acbr.acbrselfservice.repository.store_data

import br.com.acbr.acbrselfservice.entity.StoreData
import br.com.acbr.acbrselfservice.repository.configuration.ConfigurationRepository
import br.com.acbr.acbrselfservice.repository.store_data.service.*
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource

class StoreDataRepository(
    private val service: StoreDataService,
    private val configurationRepository: ConfigurationRepository
) {

    suspend fun getData(): Resource<StoreData?> {
        val rote = configurationRepository.getServerAddress()
        if(rote == null){
            return Resource(null, "Sem rota definida", ProcessStatus.Fail)
        } else {
            val response = service.getDataSync(rote)
            val data = if(response.data != null){
                StoreData(
                    logo = response.data.logo,
                    headImage = response.data.headImage,
                    ticket = response.data.ticket
                )
            } else {
                null
            }
            return Resource(data, response. message, response.status)
        }

    }
}