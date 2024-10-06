package br.com.acbr.acbrselfservice.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.acbr.acbrselfservice.entity.Cart
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.ResourceStatus

class CartViewModel (private val useCase: CartUseCase) : ViewModel() {
    private var _showProgress = MutableLiveData<Boolean?>()
    var showProgress: LiveData<Boolean?> = _showProgress

    private var _toastError = MutableLiveData<ResourceStatus?>()
    var toastError: LiveData<ResourceStatus?> = _toastError

    private var _alertError = MutableLiveData<ResourceStatus?>()
    var alertError: LiveData<ResourceStatus?> = _alertError

    private var _alertConfirmRemove = MutableLiveData<Pair<Product, Int>?>()
    var alertConfirmRemove: LiveData<Pair<Product, Int>?> = _alertConfirmRemove

    private var _showBlockingProgress = MutableLiveData<Boolean?>()
    var showBlockingProgress: LiveData<Boolean?> = _showBlockingProgress

    private var _cart = MutableLiveData<Cart?>()
    var cart: LiveData<Cart?> = _cart


    fun clearToastError(){
        _toastError.value = null
    }

    fun clearAlertError(){
        _alertError.value = null
    }

    fun clearAlertConfirmRemove(){
        _alertConfirmRemove.value = null
    }

    fun getCart(){
        if(cart.value == null){
            _showProgress.value = true
            useCase.getCart {
                _showProgress.value = false
                if(it.status == ProcessStatus.Success && it.data != null){
                    _cart.value = it.data
                } else {
                    when (it.status){
                        ProcessStatus.Success -> {  }
                        ProcessStatus.Fail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.Fail) }
                        ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                        else -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                    }
                }
            }
        }
    }

    fun getCoupon(coupon: String?){
        /*_showProgress.value = true
        useCase.getCoupon (coupon) {
            _showProgress.value = false
            if(it.status == ProcessStatus.Success && it.data != null){
                _cart.value = it.data
            } else {
                when (it.status){
                    ProcessStatus.Success -> {  }
                    ProcessStatus.Fail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.Fail) }
                    ProcessStatus.TokenExpired -> { _alertError.value = ResourceStatus(it.message, ProcessStatus.TokenExpired) }
                    ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                    else -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                }
            }
        }*/
    }

    fun confirmRemoveProduct(product: Product){
        _alertConfirmRemove.value = Pair(product, 0)
    }


    fun removeProduct(product: Product){
        _showBlockingProgress.value = true
        useCase.removeProduct (product) {
            _showBlockingProgress.value = false
            if(it.status == ProcessStatus.Success && it.data != null){
                _cart.value = it.data
            } else {
                when (it.status){
                    ProcessStatus.Success -> {  }
                    ProcessStatus.Fail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.Fail) }
                    ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                    else -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                }
            }
        }
    }
}