package br.com.acbr.acbrselfservice.ui.cart

import br.com.acbr.acbrselfservice.entity.Cart
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.repository.cart.CartRepository
import br.com.acbr.acbrselfservice.util.Resource

class CartUseCase(private val repository: CartRepository) {

    fun getCart(onPostExecute: (Resource<Cart>) -> Unit) {
        repository.getCart {
            onPostExecute(Resource(it.data, it.message, it.status))
        }
    }

    fun getCoupon(coupon: String?, onPostExecute: (Resource<Cart>) -> Unit) {
        repository.getCart {
            onPostExecute(Resource(it.data, it.message, it.status))
        }
    }

    fun removeProduct(product: Product, onPostExecute: (Resource<Cart>) -> Unit) {
        repository.deleteProduct(product) {
            onPostExecute(Resource(it.data, it.message, it.status))
        }
    }

}