package br.com.acbr.acbrselfservice.ui.configuration

import br.com.acbr.acbrselfservice.entity.ConfigurationData
import br.com.acbr.acbrselfservice.entity.StoreData
import br.com.acbr.acbrselfservice.repository.configuration.ConfigurationRepository
import br.com.acbr.acbrselfservice.repository.profile.ProfileRepository
import br.com.acbr.acbrselfservice.repository.store_data.StoreDataRepository
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource
import br.com.acbr.acbrselfservice.util.ResourceStatus

class ConfigurationUseCase (private val configurationRepository: ConfigurationRepository, private val storeDataRepository: StoreDataRepository) {


    suspend fun updateConfiguration(): Resource<ConfigurationData?> {
        val response = storeDataRepository.getData()
        if(response.status == ProcessStatus.Success && response.data != null){
//            val config = configurationRepository.getConfigurationSync()
            val conf = ConfigurationData(
                logo = response.data.logo,
                headImage = response.data.headImage,
                emitTicket = response.data.ticket
            )
            val result = configurationRepository.saveConfigurationSync(conf)
            if(result) {
                return Resource(conf, "", ProcessStatus.Success)
            } else {
                return Resource(null, "Falha salvando configurações", ProcessStatus.SaveFail)
            }
        }
        return Resource(null, response.message, response.status)
    }

    fun saveServerAddress(address: String, onPostExecute: (Boolean?) -> Unit) = configurationRepository.saveServerAddress(address, onPostExecute)

    fun getServerAddress() = configurationRepository.getServerAddress()

    fun getConfiguration(onPostExecute: (ConfigurationData) -> Unit) = configurationRepository.getConfiguration(onPostExecute)
}