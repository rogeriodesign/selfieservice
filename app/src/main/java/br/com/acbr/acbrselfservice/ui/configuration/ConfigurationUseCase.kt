package br.com.acbr.acbrselfservice.ui.configuration

import br.com.acbr.acbrselfservice.entity.ConfigurationData
import br.com.acbr.acbrselfservice.repository.configuration.ConfigurationRepository
import br.com.acbr.acbrselfservice.repository.profile.ProfileRepository
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource
import br.com.acbr.acbrselfservice.util.ResourceStatus

class ConfigurationUseCase (private val configurationRepository: ConfigurationRepository, private val profileRepository: ProfileRepository) {


    fun updateConfiguration(onPostExecute: (ResourceStatus) -> Unit) = profileRepository.updateProfile(onPostExecute)


    fun saveServerAddress(address: String, onPostExecute: (Boolean?) -> Unit) = configurationRepository.saveServerAddress(address, onPostExecute)

    fun getServerAddress(onPostExecute: (String?) -> Unit) = configurationRepository.getServerAddress(onPostExecute)

    fun saveConfiguration(configuration: ConfigurationData, onPostExecute: (Boolean) -> Unit) = configurationRepository.saveConfiguration(configuration, onPostExecute)

    fun getConfiguration(onPostExecute: (ConfigurationData) -> Unit) = configurationRepository.getConfiguration(onPostExecute)
}