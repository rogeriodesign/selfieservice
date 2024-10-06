package br.com.acbr.acbrselfservice.ui.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MenuViewModel(private val useCase: MenuUseCase) :  ViewModel(){
    private var currentQuery: String? = null
    private var currentSearchResult: Flow<PagingData<ProductUiListModel>>? = null

    fun productsFlow(search: String? = null, retry: Boolean = false): Flow<PagingData<ProductUiListModel>> {
        if (search == currentQuery && currentSearchResult != null && !retry) {
            return currentSearchResult!!
        }
        currentQuery = search

        val result = useCase.productsFlow(search)
            .map { pagingData -> pagingData.map { ProductUiListModel.ProductItem(it) } }
            .map {
                it.insertSeparators<ProductUiListModel.ProductItem, ProductUiListModel> { before, after ->
                    if (after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }
                    if (before == null) {
                        // we're at the beginning of the list
                        return@insertSeparators if(after.product.category != null) ProductUiListModel.CategoryItem(after.product.category) else null
                    }
                    // check between 2 items
                    if (after.product.category != null && after.product.category.uuid != before.product.category?.uuid) {
                        ProductUiListModel.CategoryItem(after.product.category)
                    } else {
                        // no separator
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)
        currentSearchResult = result
        return result
    }
}