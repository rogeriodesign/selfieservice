package br.com.acbr.acbrselfservice.ui.checkout.resume

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.acbr.acbrselfservice.entity.Order
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.ResourceStatus
import kotlinx.coroutines.launch

class ResumeViewModel (private val useCase: ResumeUseCase) : ViewModel() {
    private var _toastError = MutableLiveData<ResourceStatus?>()
    var toastError: LiveData<ResourceStatus?> = _toastError

    private var _alertError = MutableLiveData<ResourceStatus?>()
    var alertError: LiveData<ResourceStatus?> = _alertError

    private var _showBlockingProgress = MutableLiveData<Boolean?>()
    var showBlockingProgress: LiveData<Boolean?> = _showBlockingProgress

    private var _order = MutableLiveData<Order?>()
    var order: LiveData<Order?> = _order


    fun clearToastError(){
        _toastError.value = null
    }

    fun clearAlertError(){
        _alertError.value = null
    }

    fun postOrder(cartId: String, payment: String?, delivery: String){
        if(payment != null){
            _showBlockingProgress.value = true
            viewModelScope.launch {
                val resource = useCase.postOrder(cartId, payment, delivery)
                _showBlockingProgress.value = false
                if(resource.status == ProcessStatus.Success && resource.data != null){
                    _order.value = resource.data
                } else {
                    when (resource.status){
                        ProcessStatus.Success -> {  }
                        ProcessStatus.Fail -> { _toastError.value = ResourceStatus(resource.message, ProcessStatus.Fail) }
                        ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(resource.message, ProcessStatus.SaveFail) }
                        else -> { _toastError.value = ResourceStatus(resource.message, ProcessStatus.SaveFail) }
                    }
                }
            }
        } else {
            _alertError.value = ResourceStatus("", ProcessStatus.MissingParameter)
        }
    }

}