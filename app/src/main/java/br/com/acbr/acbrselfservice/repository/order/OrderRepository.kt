package br.com.acbr.acbrselfservice.repository.order

import br.com.acbr.acbrselfservice.entity.*
import br.com.acbr.acbrselfservice.repository.ProductDto
import br.com.acbr.acbrselfservice.repository.order.service.NewOrderDto
import br.com.acbr.acbrselfservice.repository.order.service.OrderDto
import br.com.acbr.acbrselfservice.repository.order.service.OrderService
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource

class OrderRepository (private val service: OrderService
) {
    suspend fun postOrderSync(
        cartId: String,
        payment: String,
        delivery: String,
    ): Resource<Order> {
        val resourceMalls = service.postOrderSync(
            NewOrderDto(
                cartId = cartId,
                payment = payment,
                delivery = delivery
            ),
            ""
        )
        if(resourceMalls.status == ProcessStatus.Success && resourceMalls.data != null) {
            return Resource(dtoToData(resourceMalls.data), "", ProcessStatus.Success)
        } else if(resourceMalls.message.indexOf("401", 0, true) > -1) {// token expirado
            return postOrderSync(cartId, payment, delivery)
        } else {
            return Resource(null, resourceMalls.message, resourceMalls.status)
        }
    }


    private fun dtoToData(dto: OrderDto): Order {
        return Order(
            id = dto.id,
            products = merchantDtoToData(dto.products)
        )
    }


    private fun merchantDtoToData(dto: List<ProductDto?>?): List<Product>? {
        var productsList: MutableList<Product>? = null
        if(!dto.isNullOrEmpty()) {
            productsList = mutableListOf()

            for (product in dto) {
                if (product != null) {
                    productsList.add(productDtoToData(product))
                }
            }
        }

        return productsList
    }


    private fun productDtoToData(
        product: ProductDto
    ): Product {
        var groupOptionsList: List<ProductOptionGroup?>? = null

        if (!product.optionGroups.isNullOrEmpty()) {
            val groupList = mutableListOf<ProductOptionGroup>()

            for (group in product.optionGroups) {
                var optionList: MutableList<ProductOption>? = null
                if (group.options != null) {
                    optionList = mutableListOf()
                    for (option in group.options) {
                        optionList.add(
                            ProductOption(
                                id = 0,
                                uuid = option.uuid,
                                name = option.name,//"Bacon"
                                description = option.description,//"molho extra"
                                originalValue = option.price?.originalValue,
                                value = option.price?.value,
                                imagePath = option.imagePath,
                                amount = option.amount,
                                productId = 0,
                                sequence = option.sequence,
                                status = option.status,
                                index = option.index,
                                optionGroups = option.optionGroups
                            )
                        )
                    }
                }

                groupList.add(
                    ProductOptionGroup(
                        id = group.id,//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
                        index = group.index,//0
                        maximum = group.maximum,//2
                        minimum = group.minimum,//0
                        name = group.name,//"Adicionais"
                        options = optionList
                    )
                )
            }

            groupOptionsList = groupList
        }

        return Product(
            id = 0,
            uuid = product.uuid,
            description = product.description,//"Descrição do item..."
            externalCode = null,//"c01-i001"
            imagePath = product.imagePath,//"path/path"
            index = null,//"0"
            name = product.name,//"Sanduíche"
            sequence = null,//"0"
            serving = null,//"SERVES_1"
            status = null,//"AVAILABLE"
            value = product.price?.value,
            originalValue = product.price?.originalValue,
            amount = product.amount,
            optionGroups = groupOptionsList,
            category = null,
            notes = product.note
        )
    }
}