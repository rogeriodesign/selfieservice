package br.com.acbr.acbrselfservice.ui.home

import br.com.acbr.acbrselfservice.entity.Cart
import br.com.acbr.acbrselfservice.repository.cart.CartRepository
import br.com.acbr.acbrselfservice.util.Resource

class HomeUseCase(private val repository: CartRepository) {

    fun getCart(onPostExecute: (Resource<Cart>) -> Unit) {
        repository.getCart {
            onPostExecute(Resource(it.data, it.message, it.status))
        }
    }

}