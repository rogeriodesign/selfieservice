package br.com.acbr.acbrselfservice.ui.product_list

import br.com.acbr.acbrselfservice.entity.Category
import br.com.acbr.acbrselfservice.entity.Product

sealed class ProductUiListModel {
    data class ProductItem(val product: Product) : ProductUiListModel()
    data class CategoryItem(val category: Category) : ProductUiListModel()
}