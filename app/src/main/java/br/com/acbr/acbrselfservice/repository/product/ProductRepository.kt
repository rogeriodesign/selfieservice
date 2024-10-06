package br.com.acbr.acbrselfservice.repository.product

import br.com.acbr.acbrselfservice.entity.Category
import br.com.acbr.acbrselfservice.entity.Pagination
import br.com.acbr.acbrselfservice.entity.Price
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.entity.ProductOption
import br.com.acbr.acbrselfservice.entity.ProductOptionGroup
import br.com.acbr.acbrselfservice.repository.PaginationDto
import br.com.acbr.acbrselfservice.repository.ProductDto
import br.com.acbr.acbrselfservice.repository.product.service.CategoryDto
import br.com.acbr.acbrselfservice.repository.product.service.ProductService
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource

class ProductRepository(
    private val service: ProductService
) {
    suspend fun getProductsSync(
        search: String?,
        page: Int,
        perPage: Int
    ): Resource<Pagination<Product>> {
        val resourceProduct = service.getProductsSync(
            search,
            page,
            perPage
        )
        if (resourceProduct.status == ProcessStatus.Success && resourceProduct.data != null) {
            return Resource(makeProductPagination(resourceProduct.data), "", ProcessStatus.Success)
        } else if (resourceProduct.message.indexOf("401", 0, true) > -1) {// token expirado
            return getProductsSync(search, page, perPage)
        } else {
            return Resource(null, resourceProduct.message, resourceProduct.status)
        }
    }

    suspend fun getProductsByCategorySync(
        page: Int,
        perPage: Int
    ): Resource<Pagination<Product>> {
        val resourceProduct = service.getProductsByCategorySync(
            page,
            perPage
        )
        if (resourceProduct.status == ProcessStatus.Success && resourceProduct.data != null) {
            return Resource(makeProductPagination(resourceProduct.data), "", ProcessStatus.Success)
        } else if (resourceProduct.message.indexOf("401", 0, true) > -1) {// token expirado
            return getProductsByCategorySync(page, perPage)
        } else {
            return Resource(null, resourceProduct.message, resourceProduct.status)
        }
    }

    suspend fun getProductSync(
        productId: String
    ): Resource<Product> {
        val resourceProduct = service.getProductSync(
            productId
        )
        if (resourceProduct.status == ProcessStatus.Success && resourceProduct.data != null) {
            return Resource(dtoProductToData(resourceProduct.data), "", ProcessStatus.Success)
        } else if (resourceProduct.message.indexOf("401", 0, true) > -1) {// token expirado
            return getProductSync(productId)
        } else {
            return Resource(null, resourceProduct.message, resourceProduct.status)
        }
    }

    fun getCategories(
        onPostExecute: (Resource<Pagination<Category>>) -> Unit
    ) {
        service.getCategories() {
            if (it.status == ProcessStatus.Success && it.data != null) {
                onPostExecute(
                    Resource(
                        makeCategoriesPagination(it.data),
                        "",
                        ProcessStatus.Success
                    )
                )
            } else if (it.message.indexOf("401", 0, true) > -1) {// token expirado
                getCategories(onPostExecute)
            } else {
                onPostExecute(Resource(null, it.message, it.status))
            }
        }
    }

    fun searchProducts(
        search: String,
        page: Int,
        onPostExecute: (Resource<Pagination<Product>>) -> Unit
    ) {
        service.searchProducts(
            search,
            page
        ) {
            if (it.status == ProcessStatus.Success && it.data != null) {
                onPostExecute(Resource(makeProductPagination(it.data), "", ProcessStatus.Success))
            } else if (it.message.indexOf("401", 0, true) > -1) {// token expirado
                searchProducts(search, page, onPostExecute)
            } else {
                onPostExecute(Resource(null, it.message, it.status))
            }
        }
    }

    fun getProductsByCategory(
        categoryId: String,
        page: Int,
        onPostExecute: (Resource<Pagination<Product>>) -> Unit
    ) {
        service.getProductsByCategory(
            categoryId,
            page
        ) {
            if (it.status == ProcessStatus.Success && it.data != null) {
                onPostExecute(Resource(makeProductPagination(it.data), "", ProcessStatus.Success))
            } else if (it.message.indexOf("401", 0, true) > -1) {// token expirado
                getProductsByCategory(categoryId, page, onPostExecute)
            } else {
                onPostExecute(Resource(null, it.message, it.status))
            }
        }
    }

    fun getProductPrice(
        productId: String,
        onPostExecute: (Resource<Price>) -> Unit
    ) {
        service.getProductPrice(
            productId
        ) {
            if (it.status == ProcessStatus.Success && it.data != null) {
                onPostExecute(
                    Resource(
                        Price(
                            originalValue = it.data.originalValue,
                            value = it.data.value
                        ), "", ProcessStatus.Success
                    )
                )
            } else if (it.message.indexOf("401", 0, true) > -1) {// token expirado
                getProductPrice(productId, onPostExecute)
            } else {
                onPostExecute(Resource(null, it.message, it.status))
            }
        }
    }

    private fun makeCategoriesPagination(pagination: PaginationDto<CategoryDto>): Pagination<Category> {
        return Pagination(
            currentPage = pagination.currentPage,
            data = listDtoToListData(pagination.data),
            lastPage = pagination.lastPage,
            perPage = pagination.perPage,
            total = pagination.total
        )
    }

    private fun listDtoToListData(dtos: List<CategoryDto>): List<Category> {
        val list = mutableListOf<Category>()
        for (item in dtos) {
            list.add(dtoToData(item))
        }
        return list
    }

    private fun dtoToData(dto: CategoryDto): Category {
        return Category(
            name = dto.name,//"Nome da categoria"
            status = dto.status,//"AVAILABLE"
            template = dto.template,//"PIZZA"
            uuid = dto.uuid
        )
    }

    private fun makeProductPagination(pagination: PaginationDto<ProductDto>): Pagination<Product> {
        return Pagination(
            currentPage = pagination.currentPage,
            data = listProductDtoToListData(pagination.data),
            lastPage = pagination.lastPage,
            perPage = pagination.perPage,
            total = pagination.total
        )
    }

    private fun listProductDtoToListData(dtos: List<ProductDto>): List<Product> {
        val list = mutableListOf<Product>()
        for (item in dtos) {
            list.add(dtoProductToData(item))
        }
        return list
    }

    private fun dtoProductToData(dto: ProductDto): Product {
        var optionGroupsList: List<ProductOptionGroup?>? = null

        if (!dto.optionGroups.isNullOrEmpty()) {
            val groupList = mutableListOf<ProductOptionGroup>()
            for (group in dto.optionGroups) {

                var optionList: MutableList<ProductOption>? = null
                if (group.options != null) {
                    optionList = mutableListOf()
                    for (option in group.options) {
                        optionList.add(
                            ProductOption(
                                id = 0,
                                uuid = option.uuid,
                                name = option.name,//"Bacon"
                                index = option.index,
                                description = option.description,//"molho extra"
                                originalValue = option.price?.originalValue,
                                value = option.price?.value,
                                imagePath = option.imagePath,
                                amount = option.amount,
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
            optionGroupsList = groupList
        }

        val category = if (dto.category != null) {
            Category(
                uuid = dto.category.uuid,
                name = dto.category.name,//"Nome da categoria"
                status = dto.category.status,//"AVAILABLE"
                template = dto.category.template
            )
        } else {
            null
        }

        return Product(
            id = 0,
            uuid = dto.uuid,
            description = dto.description,//"Descrição do item..."
            externalCode = dto.externalCode,//"c01-i001"
            imagePath = dto.imagePath,//"path/path"
            index = dto.index,//"0"
            name = dto.name,//"Sanduíche"
            sequence = dto.sequence,//"0"
            serving = dto.serving,//"SERVES_1"
            status = dto.status,//"AVAILABLE"
            value = dto.price?.value,
            originalValue = dto.price?.originalValue,
            amount = null,
            optionGroups = optionGroupsList,
            category = category
        )
    }
}