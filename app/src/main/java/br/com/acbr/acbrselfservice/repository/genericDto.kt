package br.com.acbr.acbrselfservice.repository

import br.com.acbr.acbrselfservice.repository.product.service.CategoryDto
import com.google.gson.annotations.SerializedName

data class PaginationDto<T> (
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("data") val data: List<T>,
    @SerializedName("last_page") val lastPage: Int?,
    @SerializedName("per_page") val perPage: Int?,
    @SerializedName("total") val total: Int?
)

data class PriceDto (
    @SerializedName("original_value") val originalValue: Double? = null,//20
    @SerializedName("value") val value: Double? = null//20
)

data class ProductOptionDto (
    @SerializedName("uuid") val uuid: String,//"f1e6d42a-484e-4c25-928c-827c0c4f3f6e"
    @SerializedName("name") val name: String,//"Refri"
    @SerializedName("description") val description: String,//"Descrição do produto"
    @SerializedName("price") val price: PriceDto? = null,//20.56
    @SerializedName("image_path") val imagePath: String? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("index") val index: Int? = null,//0
    @SerializedName("product_id") val productId: String? = null,//"2d54637a-2ea3-40dd-bce9-052226ae7471"
    @SerializedName("sequence") val sequence: Int? = null,//1
    @SerializedName("status") val status: String? = null,//"AVAILABLE"
    @SerializedName("option_groups") val optionGroups: List<OptionGroupDto>? = null
)

data class OptionGroupDto (
    @SerializedName("id") val id: String,//"94ae70e1-cb23-4b27-a83f-866a6b1400e5"
    @SerializedName("index") val index: Int? = null,//0
    @SerializedName("name") val name: String,//"Bacon"
    @SerializedName("max") val maximum: Int? = null,//"molho extra"
    @SerializedName("min") val minimum: Int? = null,//"Adicionais"
    @SerializedName("options") val options: List<ProductOptionDto>? = null
)

data class ProductDto (
    @SerializedName("uuid") val uuid: String,//"d184fd56-1f9d-42f0-b4b1-91a3cb27b11c"**
    @SerializedName("name") val name: String?,//"Sanduíche"**
    @SerializedName("description") val description: String?,//"Descrição do item..."**
    @SerializedName("dietary_restrictions") val dietaryRestrictions: List<String>?,//["ORGANIC"]
    @SerializedName("external_code") val externalCode: String?,//"c01-i001"**
    @SerializedName("image_path") val imagePath: String?,//"path/path"**
    @SerializedName("index") val index: String?,//"0"**
    @SerializedName("option_groups") val optionGroups: List<OptionGroupDto>?,//**
    @SerializedName("price") val price: PriceDto? = null,
    //@SerializedName("price") val priceList: List<PriceDto?>? = null,
    @SerializedName("sequence") val sequence: String?,//"0"**
    @SerializedName("serving") val serving: String?,//"SERVES_1" "NOT_APPLICABLE"**
    @SerializedName("status") val status: String?,//"AVAILABLE"**

    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("note") val note: String? = null,
    @SerializedName("category") val category: CategoryDto? = null
)

