package br.com.acbr.acbrselfservice.ui.product_list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import br.com.acbr.acbrselfservice.repository.product.ProductPagingSource
import br.com.acbr.acbrselfservice.repository.product.ProductRepository

class MenuUseCase (private val repository: ProductRepository) {
    fun productsFlow(search: String? = null) = Pager(
        // Configure como os dados são carregados passando propriedades adicionais para PagingConfig, como prefetchDistance.
        PagingConfig(pageSize = 1, prefetchDistance = 1, enablePlaceholders = true, initialLoadSize = 1)//)
    ) {
        ProductPagingSource(repository, search)
    }.flow
}