package br.com.acbr.acbrselfservice.repository.cart.service

import br.com.acbr.acbrselfservice.repository.*
import br.com.acbr.acbrselfservice.repository.product.service.CategoryDto
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource
import okhttp3.ResponseBody

class CartService {
    private val service = RestGenericService(5).getCartService()

    fun getCart(
        deviceUniqueId: String,
        authorization: String,
        onPostExecute: (Resource<CartDto>) -> Unit
    ){
//        service.getCart(deviceUniqueId,
//            authorization).enqueue(
//            GenericCallback<CartDto>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterado
        onPostExecute(
            Resource(
                listCard[0], "", ProcessStatus.Success
            )
        )
    }

    fun delete(
        deviceUniqueId: String,
        authorization: String,
        onPostExecute: (Resource<ResponseBody>) -> Unit
    ){
        service.delete(deviceUniqueId,
            authorization).enqueue(
            GenericCallback<ResponseBody>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun postCart(
        cart: CartDto,
        authorization: String,
        onPostExecute: (Resource<CartDto>) -> Unit
    ){
//        service.postCart(cart,
//            authorization).enqueue(
//            GenericCallback<CartDto>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterado
        onPostExecute(
            Resource(
                listCard[0], "", ProcessStatus.Success
            )
        )
    }

    fun postProduct(
        deviceUniqueId: String,
        merchantId: String,
        product: ProductDto,
        authorization: String,
        onPostExecute: (Resource<CartDto>) -> Unit
    ){
//        service.postProduct(deviceUniqueId, merchantId, product,
//            authorization).enqueue(
//            GenericCallback<CartDto>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterado
        onPostExecute(
            Resource(
                listCard[0], "", ProcessStatus.Success
            )
        )
    }

    fun putProduct(
        deviceUniqueId: String,
        merchantId: String,
        index: Int,
        product: ProductDto,
        authorization: String,
        onPostExecute: (Resource<CartDto>) -> Unit
    ){
//        service.putProduct(deviceUniqueId,
//            merchantId,
//            index,
//            product,
//            authorization).enqueue(
//            GenericCallback<CartDto>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterado
        onPostExecute(
            Resource(
                listCard[0], "", ProcessStatus.Success
            )
        )
    }

    fun deleteProduct(
        deviceUniqueId: String,
        merchantId: String,
        index: Int,
        authorization: String,
        onPostExecute: (Resource<CartDto>) -> Unit
    ){
//        service.deleteProduct(deviceUniqueId,
//            merchantId,
//            index,
//            authorization).enqueue(
//            GenericCallback<CartDto>(
//                onPostExecute = onPostExecute
//            )
//        )
        // alterado
        onPostExecute(
            Resource(
                listCard[0], "", ProcessStatus.Success
            )
        )
    }

}

val listProducts = listOf(
    ProductDto(
        description = "Refrigerante light lata - 300ml",
        price = PriceDto(7.300, 7.300),
        imagePath = "https://a-static.mlcdn.com.br/618x463/refrigerante-lata-fanta-laranja-350ml/lusitanashopping/6e318b906c7211ebbce84201ac1850d6/b0a6db1af79861cc5321422c0e2ed989.jpg",
        uuid = "d184fd56-1f9d-42f0-b4b1-91a3cb27b11c",
        name = "Refrigerante",
        dietaryRestrictions = listOf("ORGANIC"),
        externalCode = "c01-i001",
        index = "0",
        optionGroups = listOf(
            OptionGroupDto(
                id = "94ae70e1-cb23-4b27-a83f-866a6b1400e5",
                index = 0,
                name = "Tamanho",
                maximum = 1,//"molho extra",
                minimum = 1, //"Adicionais",
                options = listOf(
                    ProductOptionDto(
                        uuid = "f1e6d42a-484e-4c25-928c-827c0c4f3f6e",
                        name = "Lata",
                        description = "lata de 300 ml",
                        price = PriceDto(
                            originalValue = 20.56,
                            value = 19.56
                        ),
                        imagePath = null,
                        amount = null,
                        index = 0,
                        productId = "2d54637a-2ea3-40dd-bce9-052226ae7471",
                        sequence =1,
                        status = "AVAILABLE",
                        optionGroups = null
                    ),
                    ProductOptionDto(
                        uuid = "f1e6d42a-484e-4c25-928c-827c0c4f3f6e",
                        name = "Garrafa",
                        description = "lata de 600 ml",
                        price = PriceDto(
                            originalValue = 20.56,
                            value = 19.56
                        ),
                        imagePath = null,
                        amount = null,
                        index = 0,
                        productId = "2d54637a-2ea3-40dd-bce9-052226ae7471",
                        sequence =1,
                        status = "AVAILABLE",
                        optionGroups = null
                    )
                )
            )
        ),
        //@SerializedName("price") val priceList: List<PriceDto?>? = null,
        sequence = "0",
        serving = "SERVES_1",
        status = "AVAILABLE",

        amount = null,
        note = null,
        category = null
    ),
    ProductDto(
        name = "Cheese Salada",
        description = "Sanduiche com pão de hamburger, queijo mussarela, fatias de tomate, folha de alface, um hamburguer de carne bovina de 50g, molho de catchup, mostarda e maionese.",
        price = PriceDto(18.500, 18.500),
        imagePath = "https://www.montarumnegocio.com/wp-content/uploads/2017/06/Como-fazer-x-Salada-na-chapa-para-vender.jpg",
        uuid = "d184fd56-1f9d-42f0-b4b1-91a3cb27b11c",
        dietaryRestrictions = listOf("ORGANIC"),
        externalCode = "c01-i001",
        index = "0",
        optionGroups = listOf(
            OptionGroupDto(
                id = "94ae70e1-cb23-4b27-a83f-866a6b1400e5",
                index = 0,
                name = "Guardanapo",
                maximum = 3,//"molho extra",
                minimum = 1, //"Adicionais",
                options = listOf(
                    ProductOptionDto(
                        uuid = "f1e6d42a-484e-4c25-928c-827c0c4f3f6e",
                        name = "Guardanapo",
                        description = "Descrição do produto",
                        price = PriceDto(
                            originalValue = 20.56,
                            value = 19.56
                        ),
                        imagePath = null,
                        amount = null,
                        index = 0,
                        productId = "2d54637a-2ea3-40dd-bce9-052226ae7471",
                        sequence =1,
                        status = "AVAILABLE",
                        optionGroups = null
                    )
                )
            ),
            OptionGroupDto(
                id = "94ae70e1-cb23-4b27-a83f-866a6b1400e5",
                index = 0,
                name = "Molho extra",
                maximum = 3,//"molho extra",
                minimum = 0, //"Adicionais",
                options = listOf(
                    ProductOptionDto(
                        uuid = "f1e6d42a-484e-4c25-928c-827c0c4f3f6e",
                        name = "Molho especial",
                        description = "Descrição do produto",
                        price = PriceDto(
                            originalValue = 20.56,
                            value = 19.56
                        ),
                        imagePath = null,
                        amount = null,
                        index = 0,
                        productId = "2d54637a-2ea3-40dd-bce9-052226ae7471",
                        sequence =1,
                        status = "AVAILABLE",
                        optionGroups = null
                    )
                )
            )
        ),
        //@SerializedName("price") val priceList: List<PriceDto?>? = null,
        sequence = "0",
        serving = "SERVES_1",
        status = "AVAILABLE",

        amount = null,
        note = null,
        category = null
    )

)


val listCard = listOf(
    CartDto(
        total = 28.00,
        products = listProducts
    )
)



val listCategory = listOf(
    CategoryDto(
        uuid = "2f862147-33ae-476d-b313-772eebd36b54",
        name = "Pizza",
        status = "AVAILABLE",
        template = "PIZZA"
    ),
    CategoryDto(
        uuid = "2f862147-33ae-476d-b313-772eebd36b54",
        name = "Refrigerante",
        status = "AVAILABLE",
        template = "BEBIDAS"
    )
)

//val logo: String?,//"https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/Burger_King_2020.svg/140px-Burger_King_2020.svg.png"
//    val backgroundImage: String?,//"https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/Burger_King_2020.svg/140px-Burger_King_2020.svg.png"