package br.com.acbr.acbrselfservice.ui.product

import br.com.acbr.acbrselfservice.entity.Cart
import br.com.acbr.acbrselfservice.entity.Price
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.repository.cart.CartRepository
import br.com.acbr.acbrselfservice.repository.product.ProductRepository
import br.com.acbr.acbrselfservice.util.Resource

class ProductUseCase(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) {
    fun getProductPrice(
        productId: String,
        onPostExecute: (Resource<Price>) -> Unit
    ) {
        productRepository.getProductPrice(
            productId,
            onPostExecute
        )
    }

    suspend fun getProductSync(productId: String) = productRepository.getProductSync(productId)

    fun addProductInCart(product: Product, onPostExecute: (Resource<Cart>) -> Unit) {
        cartRepository.addProduct(product, onPostExecute)
    }

    fun updateProductInCart(product: Product, onPostExecute: (Resource<Cart>) -> Unit) {
        cartRepository.updateProduct(product, onPostExecute)
    }
}