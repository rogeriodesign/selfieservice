package br.com.acbr.acbrselfservice.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.acbr.acbrselfservice.entity.Cart
import br.com.acbr.acbrselfservice.util.ProcessStatus

class HomeViewModel (private val useCase: HomeUseCase) : ViewModel() {

    private var _gotCart = MutableLiveData<Boolean?>()
    private var gotCart: LiveData<Boolean?> = _gotCart

    private var _cart = MutableLiveData<Cart?>()
    var cart: LiveData<Cart?> = _cart

    private var _paymentToken = MutableLiveData<String?>()
    var paymentToken: LiveData<String?> = _paymentToken

    fun getCart(retry:Boolean = false){
        if((cart.value == null && (gotCart.value == null || gotCart.value == false) || retry)){
            useCase.getCart {
                // o gotCart serve para que ao rotacionar a tela a busca não seja refeita
                // com isso outra busca só será possivel com retry==true
                _gotCart.value = true
                if(it.status == ProcessStatus.Success && it.data != null){
                    if(!it.data.products.isNullOrEmpty()) {
                        setCurrentCart(it.data)
                    }
                } else {
                    /*when (it.status){
                        ProcessStatus.Success -> {  }
                        ProcessStatus.Fail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.Fail) }
                        ProcessStatus.TokenExpired -> { _alertError.value = ResourceStatus(it.message, ProcessStatus.TokenExpired) }
                        ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                        else -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                    }*/
                }
            }
        }
    }

    fun setCurrentCart(cart: Cart){
        _cart.value = cart
    }

    fun setPayment(token: String?){
        _paymentToken.value = token
    }
}

