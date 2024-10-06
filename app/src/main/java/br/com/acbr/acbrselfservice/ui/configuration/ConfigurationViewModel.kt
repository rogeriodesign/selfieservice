package br.com.acbr.acbrselfservice.ui.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.acbr.acbrselfservice.entity.ConfigurationData
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.ResourceStatus

class ConfigurationViewModel (private val useCase: ConfigurationUseCase) : ViewModel() {
    private var _showProgress = MutableLiveData<Boolean?>()
    var showProgress: LiveData<Boolean?> = _showProgress

    private var _toastError = MutableLiveData<ResourceStatus?>()
    var toastError: LiveData<ResourceStatus?> = _toastError

    private var _alertError = MutableLiveData<ResourceStatus?>()
    var alertError: LiveData<ResourceStatus?> = _alertError

    private var _serverAddress = MutableLiveData<String>()
    var serverAddress: LiveData<String> = _serverAddress

    private var _configuration = MutableLiveData<ConfigurationData>()
    var configuration: LiveData<ConfigurationData> = _configuration


    init {
        getServerAddress{
            _serverAddress.value = it
        }
        getConfiguration {
            _configuration.value = it
        }
    }


    fun saveServerAddress(address: String, onPostExecute: (Boolean?) -> Unit) = useCase.saveServerAddress(address, onPostExecute)

    private fun getServerAddress(onPostExecute: (String?) -> Unit) = useCase.getServerAddress(onPostExecute)

    fun saveConfiguration(configuration: ConfigurationData, onPostExecute: (Boolean) -> Unit) = useCase.saveConfiguration(configuration, onPostExecute)

    private fun getConfiguration(onPostExecute: (ConfigurationData) -> Unit) = useCase.getConfiguration(onPostExecute)

    fun updateProfile(){
        _showProgress.value = true
        useCase.updateConfiguration{
            _showProgress.value = false
            when (it.status){
                ProcessStatus.Success -> {  }
                ProcessStatus.Fail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.Fail) }
                ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                ProcessStatus.FailExternalAPI -> {}
                ProcessStatus.MissingParameter -> {}
            }
        }
    }

    fun saveServerAddress(serverAddress: String) {
        if(this.serverAddress.value != serverAddress){
            _showProgress.value = true
//            useCase.saveNameProfile(serverAddress){
//                _showProgress.value = false
//                if(it.status == ProcessStatus.Success && it.data != null){
//                    _toastError.value = ResourceStatus("", ProcessStatus.Success)
//                } else {
//                    when (it.status){
//                        ProcessStatus.Success -> { }
//                        ProcessStatus.Fail -> { _toastError.value = ResourceStatus("Código inválido!", ProcessStatus.Fail) }
//                        ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
//                        ProcessStatus.FailExternalAPI -> {}
//                        ProcessStatus.MissingParameter -> {}
//                    }
//                }
//            }
        }
    }

    fun clearToastError(){
        _toastError.value = null
    }

    fun clearAlertError(){
        _alertError.value = null
    }
}