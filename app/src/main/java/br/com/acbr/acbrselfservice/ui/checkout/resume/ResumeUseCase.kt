package br.com.acbr.acbrselfservice.ui.checkout.resume

import br.com.acbr.acbrselfservice.entity.Order
import br.com.acbr.acbrselfservice.repository.order.OrderRepository
import br.com.acbr.acbrselfservice.util.Resource

class ResumeUseCase (private val repository: OrderRepository) {

    suspend fun postOrder(cartId: String, payment: String, delivery: String): Resource<Order>{
        return repository.postOrderSync(cartId, payment, delivery)
    }
}