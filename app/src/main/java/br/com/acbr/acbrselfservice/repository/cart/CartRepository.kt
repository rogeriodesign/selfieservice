package br.com.acbr.acbrselfservice.repository.cart

import android.content.Context
import br.com.acbr.acbrselfservice.entity.Cart
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.entity.ProductOption
import br.com.acbr.acbrselfservice.entity.ProductOptionGroup
import br.com.acbr.acbrselfservice.repository.AppDatabase
import br.com.acbr.acbrselfservice.repository.cart.database.ProductEntity
import br.com.acbr.acbrselfservice.repository.cart.database.ProductOptionEntity
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartRepository (context: Context) {
    private val daoProduct = AppDatabase.getInstance(context).productDAO
    private val daoOption = AppDatabase.getInstance(context).productOptionDAO
    private val scope = CoroutineScope(Dispatchers.Main)

    fun getCart(
        onPostExecute: (Resource<Cart>) -> Unit
    ){
        scope.launch {
            var list: MutableList<Product>? = null
            var total = 0.0
            val products = getAllSync()
            if(products.isNotEmpty()) {
                list = mutableListOf()
                for (product in products) {
                    var listO: MutableList<ProductOptionGroup>? = null
                    val options = getAllOptionSync(product.id?:0)
                    if(options.isNotEmpty()) {
                        listO = mutableListOf()
                        for (option in options){
                            listO.add(
                                ProductOptionGroup (
                                    id = "",//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
                                    index = null,//0
                                    maximum = null,//2
                                    minimum = null,//0
                                    name = null,//"Adicionais"
                                    options = listOf(
                                        ProductOption(
                                            id = option.id?:0,
                                            uuid = option.uuid,
                                            productId = option.productId,
                                            description = option.description,
                                            name = option.name,
                                            originalValue = option.originalValue,
                                            value = option.value,
                                            sequence = option.sequence,
                                            status = option.status,
                                            imagePath = option.imagePath,
                                            amount = option.amount,
                                            index = option.index,
                                            optionGroups = null
                                        )
                                    )
                                )
                            )
                            total += option.amount?:0.0
                        }
                    }
                    list.add(
                        Product(
                            id = product.id?:0,
                            uuid = product.uuid,//"d184fd56-1f9d-42f0-b4b1-91a3cb27b11c"
                            name = product.name,//"Sanduíche"
                            description = product.description,//"Descrição do item..."
                            externalCode = product.externalCode,//"c01-i001"
                            imagePath = product.imagePath,//"path/path"
                            index = product.index,//"0"
                            sequence = product.sequence,//"0"
                            serving = product.serving,//"SERVES_1"
                            status = product.status,//"AVAILABLE"
                            originalValue = product.originalValue,//20
                            value = product.value,
                            amount = product.amount,
                            optionGroups = listO,
                            category = null,
                            notes = product.notes
                        )
                    )
                    total += product.amount?:0.0
                }
            }

            onPostExecute(
                Resource(
                    Cart(
                        total = total,
                        products = list
                    ),
                    "",
                    ProcessStatus.Success
                )
            )// onPostExecute(Resource(null, it.message, it.status))
        }
    }

    fun addProduct(
        product: Product,
        onPostExecute: (Resource<Cart>) -> Unit
    ){
        scope.launch {
            val response = saveSync(
                ProductEntity(
                    uuid = product.uuid,//"d184fd56-1f9d-42f0-b4b1-91a3cb27b11c"
                    name = product.name,//"Sanduíche"
                    description = product.description,//"Descrição do item..."
                    externalCode = product.externalCode,//"c01-i001"
                    imagePath = product.imagePath,//"path/path"
                    index = product.index,//"0"
                    sequence = product.sequence,//"0"
                    serving = product.serving,//"SERVES_1"
                    status = product.status,//"AVAILABLE"
                    originalValue = product.originalValue,//20
                    value = product.value,
                    amount = product.amount,
                    categoryUuid = product.category?.uuid,
                    notes = product.notes
                )
            )

            if(!product.optionGroups.isNullOrEmpty()) {
                val listO = mutableListOf<ProductOptionEntity>()
                for (group in product.optionGroups) {
                    if(group != null && !group.options.isNullOrEmpty()) {
                        for (option in group.options) {
                            if(option != null) {
                                listO.add(
                                    ProductOptionEntity(
                                        uuid = option.uuid,
                                        productId = option.productId?:0L,
                                        description = option.description,
                                        name = option.name,
                                        originalValue = option.originalValue,
                                        value = option.value,
                                        sequence = option.sequence,
                                        status = option.status,
                                        imagePath = option.imagePath,
                                        amount = option.amount,
                                        index = option.index
                                    )
                                )
                            }
                        }
                    }
                }
                val responseOption = saveOptionSync(listO)
            }


            if(response != null && response != 0L){
                getCart {
                    if(it.status == ProcessStatus.Success && it.data != null){
                        onPostExecute(Resource(it.data, "", ProcessStatus.Success))
                    } else {
                        onPostExecute(Resource(null, "Falha ao retornar a sacola", ProcessStatus.Fail))
                    }
                }
            } else {
                onPostExecute(Resource(null, "Falha ao salvar produto", ProcessStatus.SaveFail))
            }
        }
    }

    fun updateProduct(
        product: Product,
        onPostExecute: (Resource<Cart>) -> Unit
    ){

        addProduct(product, onPostExecute)
    }

    fun deleteProduct(
        product: Product,
        onPostExecute: (Resource<Cart>) -> Unit
    ){
        scope.launch {
            deleteSync(product.id)
            deleteOptionSync(product.id)
            getCart {
                if(it.status == ProcessStatus.Success && it.data != null){
                    onPostExecute(Resource(it.data, "", ProcessStatus.Success))
                } else {
                    onPostExecute(Resource(null, "Falha ao retornar a sacola", ProcessStatus.Fail))
                }
            }
        }
    }

    private suspend fun getAllSync () = withContext(Dispatchers.IO) {
        val item = daoProduct.getAll()
        item
    }

    private suspend fun findSync (
        uuid: String
    ) = withContext(Dispatchers.IO) {
        val item = daoProduct.findForId(uuid)
        item
    }

    private suspend fun saveSync (
        product: ProductEntity
    ) = withContext(Dispatchers.IO) {
        val item = daoProduct.save(product)
        item
    }

    private suspend fun saveSync (
        products: List<ProductEntity>
    ) = withContext(Dispatchers.IO) {
        val item = daoProduct.save(products)
        item
    }

    private suspend fun removeSync (
        product: ProductEntity
    ) = withContext(Dispatchers.IO) {
        val item = daoProduct.remove(product)
        item
    }

    private suspend fun deleteSync (
        id: Long
    ) = withContext(Dispatchers.IO) {
        val item = daoProduct.delete(id)
        item
    }




    private suspend fun getAllOptionSync (
        productId: Long
    ) = withContext(Dispatchers.IO) {
        val item = daoOption.getAll(productId)
        item
    }

    private suspend fun findOptionSync (
        uuid: String
    ) = withContext(Dispatchers.IO) {
        val item = daoOption.findForId(uuid)
        item
    }

    private suspend fun saveOptionSync (
        option: ProductOptionEntity
    ) = withContext(Dispatchers.IO) {
        val item = daoOption.save(option)
        item
    }

    private suspend fun saveOptionSync (
        options: List<ProductOptionEntity>
    ) = withContext(Dispatchers.IO) {
        val item = daoOption.save(options)
        item
    }

    private suspend fun removeOptionSync (
        options: ProductOptionEntity
    ) = withContext(Dispatchers.IO) {
        val item = daoOption.remove(options)
        item
    }

    private suspend fun deleteOptionSync (
        productId: Long
    ) = withContext(Dispatchers.IO) {
        val item = daoOption.delete(productId)
        item
    }


}