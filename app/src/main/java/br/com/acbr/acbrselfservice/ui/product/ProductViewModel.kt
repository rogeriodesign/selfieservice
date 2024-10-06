package br.com.acbr.acbrselfservice.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.acbr.acbrselfservice.entity.*
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.ResourceStatus
import kotlinx.coroutines.launch

class ProductViewModel (private val useCase: ProductUseCase) : ViewModel() {
    private var _showProgress = MutableLiveData<Boolean?>()
    var showProgress: LiveData<Boolean?> = _showProgress

    private var _showBlockingProgress = MutableLiveData<Boolean?>()
    var showBlockingProgress: LiveData<Boolean?> = _showBlockingProgress

    private var _toastError = MutableLiveData<ResourceStatus?>()
    var toastError: LiveData<ResourceStatus?> = _toastError

    private var _alertError = MutableLiveData<ResourceStatus?>()
    var alertError: LiveData<ResourceStatus?> = _alertError

    private var _workProducts = MutableLiveData<Pair<Product,Product>?>()
    var workProducts: LiveData<Pair<Product,Product>?> = _workProducts

    private var _quantity = MutableLiveData<Double?>()
    var quantity: LiveData<Double?> = _quantity

    private var _priceCloud = MutableLiveData<Price?>()
    private var priceCloud: LiveData<Price?> = _priceCloud

    private var _notesCurrent = MutableLiveData<String?>()
    private var notesCurrent: LiveData<String?> = _notesCurrent

    private var _total = MutableLiveData<Double?>()
    var total: LiveData<Double?> = _total

    private var _productSend = MutableLiveData<Cart?>()
    var productSend: LiveData<Cart?> = _productSend


    fun clearToastError(){
        _toastError.value = null
    }

    fun clearAlertError(){
        _alertError.value = null
    }

    fun getProductPrice(
                      productId: String
    ){
        if(priceCloud.value == null || (priceCloud.value != null && priceCloud.value!!.originalValue == 0.000 && priceCloud.value!!.value == 0.000)){
            _priceCloud.value = Price(0.000,0.000)

            _showProgress.value = true
            useCase.getProductPrice (productId) {
                _showProgress.value = false
                if(it.status == ProcessStatus.Success && it.data != null){
                    _priceCloud.value = it.data
                    updateProductValue(value = it.data.value, originalValue = it.data.originalValue)
                } else {
                    _priceCloud.value = null
                    when (it.status){
                        ProcessStatus.Success -> {  }
                        ProcessStatus.Fail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.Fail) }
                        ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                        else -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                    }
                }
            }
        }
    }

    fun addProductInCart(){
        updateNotes(notesCurrent.value)

        workProducts.value?.let { products ->
            _showBlockingProgress.value = true

            val error = validation(products)

            if(error.isNotBlank()){
                _showBlockingProgress.value = false
                _alertError.value = ResourceStatus(error, ProcessStatus.MissingParameter)
            } else {
                useCase.addProductInCart (products.second) {
                    _showBlockingProgress.value = false

                    if(it.status == ProcessStatus.Success && it.data != null){
                        _productSend.value = it.data
                    } else if(it.message.indexOf("field is required", 0, true) > -1){
                        val erro = makeErrorField(it.message, products)

                        _alertError.value = ResourceStatus(erro, ProcessStatus.MissingParameter)
                    } else {
                        when (it.status){
                            ProcessStatus.Success -> {  }
                            ProcessStatus.Fail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.Fail) }
                            ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                            else -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                        }
                    }
                }
            }
        }
    }

    fun updateProductInCart(){
        updateNotes(notesCurrent.value)

        workProducts.value?.let { products ->
            _showBlockingProgress.value = true

            val error = validation(products)
            if(error.isNotBlank()){
                _showBlockingProgress.value = false
                _alertError.value = ResourceStatus(error, ProcessStatus.MissingParameter)
            } else {
                useCase.updateProductInCart (products.second) {
                    _showBlockingProgress.value = false

                    if(it.status == ProcessStatus.Success && it.data != null){
                        _productSend.value = it.data
                    } else if(it.message.indexOf("field is required", 0, true) > -1){
                        val erro = makeErrorField(it.message, products)

                        _alertError.value = ResourceStatus(erro, ProcessStatus.MissingParameter)
                    } else {
                        when (it.status){
                            ProcessStatus.Success -> {  }
                            ProcessStatus.Fail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.Fail) }
                            ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                            else -> { _toastError.value = ResourceStatus(it.message, ProcessStatus.SaveFail) }
                        }
                    }
                }
            }
        }
    }

    fun initEditableProduct(modelProduct: Product?, editableProduct: Product?){
        if(this.workProducts.value == null) {
            _showBlockingProgress.value = true
            _notesCurrent.value = editableProduct?.notes
            if(modelProduct != null){
                if(editableProduct != null){
                    _workProducts.value = Pair(modelProduct, editableProduct)
                    _quantity.value = editableProduct.amount
                } else {
                    _workProducts.value = Pair(
                        modelProduct,
                        makeEditableProduct(modelProduct)
                    )
                    _quantity.value = 1.000
                }
                updateTotal()
                _showBlockingProgress.value = false
            } else {
                viewModelScope.launch {
                    val resource = useCase.getProductSync(editableProduct?.uuid ?: "")
                    if(resource.status == ProcessStatus.Success && resource.data != null){
                        if (editableProduct != null) {
                            _workProducts.value = Pair(resource.data, editableProduct)
                            _quantity.value = editableProduct.amount
                        } else {
                            _workProducts.value = Pair(
                                resource.data,
                                makeEditableProduct(resource.data)
                            )
                            _quantity.value = 1.000
                        }
                        updateTotal()
                        _showBlockingProgress.value = false
                    } else {
                        _showBlockingProgress.value = false
                        _workProducts.value = null
                        if (resource.message.indexOf("404", 0, true) == -1) {
                            when (resource.status){
                                ProcessStatus.Fail -> { _toastError.value = ResourceStatus(resource.message, ProcessStatus.Fail) }
                                ProcessStatus.SaveFail -> { _toastError.value = ResourceStatus(resource.message, ProcessStatus.SaveFail) }
                                else -> { _toastError.value = ResourceStatus(resource.message, ProcessStatus.Fail) }
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateQuantity(quantity: Double){
        _quantity.value = quantity
        updateProductValue(amount = quantity)
    }

    fun updateObservation(observation: String?){
        if(observation != _workProducts.value?.second?.notes) {
            _notesCurrent.value = observation
        }
    }

    fun addOption (option: ProductOption, group: ProductOptionGroup){
        val optionNew = setProductOption(option, 1.000)
        val optionsGroupOld = workProducts.value?.second?.optionGroups

        val optionsGroupNew = if(optionsGroupOld == null){
            listOf(ProductOptionGroup(
                id = group.id,//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
                index = group.index,//0
                maximum = group.maximum,//2
                minimum = group.minimum,//0
                name = group.name,//"Adicionais"
                options = listOf(optionNew)
            ))
        } else {
            val groupListNew = mutableListOf<ProductOptionGroup>()
            var existGroup = false
            for (groupOld in optionsGroupOld){
                if(groupOld?.id == group.id) {
                    existGroup = true
                    val optionListNew = mutableListOf<ProductOption?>()
                    if(groupOld?.options != null) {
                        // trecho comentado adiciona option
                        /*optionListNew.addAll(groupOld.options)
                        if(optionListNew.size < groupOld.maximum ?: 0){
                            optionListNew.add(optionNew)
                        }*/
                        // trecho abaixo acrescenta quantidade a itens existentes
                        var optionsOldTotal = 0.000
                        for (optionCount in groupOld.options) {
                            optionsOldTotal += optionCount?.amount?:0.000
                        }

                        // o grupo de options dos selecionados não tem valor de maximum
                        var maxOptions = groupOld.maximum?.toDouble() ?: (optionsOldTotal+1.000)
                        if(groupOld.maximum == null){
                            val optionsGroupModel = workProducts.value?.first?.optionGroups
                            if (optionsGroupModel != null) {
                                for (optionModel in optionsGroupModel) {
                                    if (optionModel?.id == groupOld.id) {
                                        maxOptions = optionModel?.maximum?.toDouble() ?: maxOptions
                                        break
                                    }
                                }
                            }
                        }

                        if(optionsOldTotal < maxOptions) {
                            var optionNewExist = false
                            for (optionOld in groupOld.options) {
                                if (optionOld?.id == optionNew.id) {
                                    optionNewExist = true
                                    optionListNew.add(setProductOption(optionOld, ((optionOld.amount?:1.000) + 1.000)))
                                } else {
                                    optionListNew.add(optionOld)
                                }
                            }
                            if(!optionNewExist){
                                optionListNew.add(optionNew)
                            }
                        } else {
                            optionListNew.addAll(groupOld.options)
                        }
                        // fim trecho acrescenta quantidade
                    } else {
                        optionListNew.add(optionNew)
                    }
                    groupListNew.add(ProductOptionGroup(
                        id = groupOld?.id,//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
                        index = groupOld?.index,//0
                        maximum = groupOld?.maximum,//2
                        minimum = groupOld?.minimum,//0
                        name = groupOld?.name,//"Adicionais"
                        options = optionListNew
                    ))
                } else {
                    groupOld?.let { groupListNew.add(it) }
                }
            }
            if(!existGroup){
                groupListNew.add(ProductOptionGroup(
                    id = group.id,//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
                    index = group.index,//0
                    maximum = group.maximum,//2
                    minimum = group.minimum,//0
                    name = group.name,//"Adicionais"
                    options = listOf(optionNew)
                ))
            }

            groupListNew
        }
        updateProductValue(optionsGroup = optionsGroupNew)
    }

    fun removeOption (option: ProductOption, group: ProductOptionGroup){
        val optionsGroupOld = workProducts.value?.second?.optionGroups
        val optionsGroupNew = if(optionsGroupOld == null){
            null
        } else {
            val groupListNew = mutableListOf<ProductOptionGroup>()

            for (groupOld in optionsGroupOld){
                if(groupOld?.id == group.id) {
                    var optionListNew: MutableList<ProductOption?>? = null
                    if(groupOld?.options != null) {
                        optionListNew = mutableListOf()
                        // trecho comentado remove um option
                        /*var optionExcluded = false
                        for(optionCurrent in groupOld.options){
                            if(optionCurrent?.id != option.id){
                                optionListNew.add(optionCurrent)
                            } else if(optionExcluded) {
                                optionListNew.add(optionCurrent)
                            } else {
                                optionExcluded = true
                            }
                        }*/
                        // trecho abaixo remove quantidade de um option
                        for(optionOld in groupOld.options){
                            if(optionOld?.id == option.id){
                                if(optionOld.amount != null && optionOld.amount > 1){
                                    optionListNew.add(setProductOption(optionOld, optionOld.amount - 1))
                                }
                            } else  {
                                optionListNew.add(optionOld)
                            }
                        }
                        // fim trecho remove quantidade
                    }
                    groupListNew.add(ProductOptionGroup(
                        id = groupOld?.id,//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
                        index = groupOld?.index,//0
                        maximum = groupOld?.maximum,//2
                        minimum = groupOld?.minimum,//0
                        name = groupOld?.name,//"Adicionais"
                        options = optionListNew
                    ))
                } else {
                    groupOld?.let { groupListNew.add(it) }
                }
            }
            if(groupListNew.isEmpty()){
                null
            } else {
                groupListNew
            }
        }
        updateProductValue(optionsGroup = optionsGroupNew)
    }

    fun addRemoveOptionRadio (option: ProductOption, group: ProductOptionGroup) {
        val optionsGroupOld = workProducts.value?.second?.optionGroups
        if(optionsGroupOld != null) {
            var exist = false
            for (groupOld in optionsGroupOld) {
                if (groupOld?.id == group.id) {
                    if (groupOld?.options != null) {
                        for(optionOld in groupOld.options) {
                            if (optionOld?.id == option.id) {
                                exist = true
                                break
                            }
                        }
                    }
                    break
                }
            }
            if(exist){
                removeOption (option, group)
            } else {
                addOptionRadio (option, group)
            }
        } else {
            addOptionRadio (option, group)
        }
    }

    private fun validation(products: Pair<Product, Product>): String {
        var error = ""

        if (products.first.optionGroups != null) {
            for (groupModel in products.first.optionGroups!!) {
                if (groupModel?.minimum != null && groupModel.minimum > 0) {
                    if (products.second.optionGroups != null) {
                        var quantitySelected = 0.000
                        for (groupFinish in products.second.optionGroups!!) {
                            if (groupFinish?.id == groupModel.id) {
                                if (groupFinish?.options != null) {
                                    for (option in groupFinish.options) {
                                        if (option?.amount != null) {
                                            quantitySelected += option.amount
                                        }
                                    }
                                }
                                break
                            }
                        }
                        if (quantitySelected < groupModel.minimum) {
                            error += "\nÉ necessário incluir no mínimo ${groupModel.minimum} ${if (groupModel.minimum > 1) "itens" else "item"} do grupo \"${groupModel.name}\""
                        }
                    } else {
                        error += "\nÉ necessário incluir no mínimo ${groupModel.minimum} ${if (groupModel.minimum > 1) "itens" else "item"} do grupo \"${groupModel.name}\""
                    }
                }
            }
        }
        return error
    }

    private fun makeErrorField(
        message: String,
        products: Pair<Product, Product>
    ): String {
        //{"option_groups.0.options":["The option_groups.0.options field is required."]}
        var erro = "Um item é requerido"
        var numberString = ""
        val initialIndex = message.indexOf("option_groups.", 0, true)
        if (initialIndex > -1) {
            for ((i, caracter) in message.withIndex()) {
                if (i > initialIndex + 14 && caracter.toString() != ".") {
                    numberString += caracter
                    if (caracter.toString() == ".") {
                        break
                    }
                }
            }
        }
        if (numberString.isNotBlank()) {
            val index = numberString.toInt()
            val optionGroup = products.first.optionGroups?.get(index)
            erro =
                "É necessário incluir no mínimo ${optionGroup?.minimum} ${if (optionGroup?.minimum ?: 0 > 1) "itens" else "item"} do grupo \"${optionGroup?.name}\""
        }
        return erro
    }

    private fun makeEditableProduct(modelProduct: Product) =
        Product(
            id = modelProduct.id,
            uuid = modelProduct.uuid,//"d184fd56-1f9d-42f0-b4b1-91a3cb27b11c"
            name = modelProduct.name,//"Sanduíche"
            description = modelProduct.description,//"Descrição do item..."
            externalCode = modelProduct.externalCode,//"c01-i001"
            imagePath = modelProduct.imagePath,//"path/path"
            index = modelProduct.index,//"0"
            sequence = modelProduct.sequence,//"0"
            serving = modelProduct.serving,//"SERVES_1"
            status = modelProduct.status,//"AVAILABLE"
            originalValue = modelProduct.originalValue,//20
            value = modelProduct.value,
            amount = 1.000,
            optionGroups = null,
            category = modelProduct.category,
            notes = modelProduct.notes
        )

    private fun getOriginalValue (default: Double?): Double? {
        return if(priceCloud.value != null && priceCloud.value!!.originalValue != null && priceCloud.value!!.originalValue!! > 0 &&
            priceCloud.value!!.value != null && priceCloud.value!!.value!! > 0) {
            priceCloud.value!!.originalValue!!
        } else {
            default
        }
    }

    private fun getValue (default: Double?): Double? {
        return if(priceCloud.value != null && priceCloud.value!!.originalValue != null && priceCloud.value!!.originalValue!! > 0 &&
            priceCloud.value!!.value != null && priceCloud.value!!.value!! > 0) {
            priceCloud.value!!.value!!
        } else {
            default
        }
    }

    private fun updateProductValue(value: Double? = null, originalValue: Double? = null, amount: Double? = null, observation: String? = null, optionsGroup: List<ProductOptionGroup>? = null){
        workProducts.value?.let { products ->
            _workProducts.value = Pair(products.first,
                Product(
                    id = products.second.id,
                    uuid = products.second.uuid,//"d184fd56-1f9d-42f0-b4b1-91a3cb27b11c"
                    name = products.second.name,//"Sanduíche"
                    description = products.second.description,//"Descrição do item..."
                    externalCode = products.second.externalCode,//"c01-i001"
                    imagePath = products.second.imagePath,//"path/path"
                    index = products.second.index,//"0"
                    sequence = products.second.sequence,//"0"
                    serving = products.second.serving,//"SERVES_1"
                    status = products.second.status,//"AVAILABLE"
                    originalValue = getOriginalValue(originalValue ?: products.second.originalValue),//20
                    value = getValue(value ?: products.second.value),
                    amount = amount ?: products.second.amount,
                    optionGroups = optionsGroup ?: products.second.optionGroups,
                    category = products.second.category,
                    notes = observation ?: notesCurrent.value?: products.second.notes
                )
            )

            updateTotal()
        }
    }

    private fun updateNotes(notes: String?){
        workProducts.value?.let { products ->
            val new = Pair(products.first,
                Product(
                    id = products.second.id,
                    uuid = products.second.uuid,//"d184fd56-1f9d-42f0-b4b1-91a3cb27b11c"
                    name = products.second.name,//"Sanduíche"
                    description = products.second.description,//"Descrição do item..."
                    externalCode = products.second.externalCode,//"c01-i001"
                    imagePath = products.second.imagePath,//"path/path"
                    index = products.second.index,//"0"
                    sequence = products.second.sequence,//"0"
                    serving = products.second.serving,//"SERVES_1"
                    status = products.second.status,//"AVAILABLE"
                    originalValue = getOriginalValue(products.second.originalValue),//20
                    value = getValue(products.second.value),
                    amount = products.second.amount,
                    optionGroups = products.second.optionGroups,
                    category = products.second.category,
                    notes = notes ?: products.second.notes
                )
            )
            _workProducts.value = new
        }
    }

    private fun updateTotal(){
        var geral = workProducts.value?.second?.value ?: 0.000
        if(workProducts.value?.second?.optionGroups != null){
            for (group in workProducts.value!!.second.optionGroups!!){
                if(group?.options != null) {
                    for (option in group.options) {
                        if(option?.value != null){
                            geral += option.value * (option.amount?:1.000)
                        }
                    }
                }
            }
        }
        _total.value = geral * (quantity.value ?: 0.000)
    }

    private fun setProductOption(option: ProductOption, amount: Double) =
        ProductOption(
            id = option.id,
            uuid = option.uuid,
            description = option.description,
            name = option.name,
            originalValue = option.originalValue,
            value = option.value,
            productId = option.productId,
            sequence = option.sequence,
            status = option.status,
            imagePath = option.imagePath,
            amount = amount,
            index = option.index,
            optionGroups = option.optionGroups
        )


    private fun addOptionRadio (option: ProductOption, group: ProductOptionGroup){
        val optionsGroupOld = workProducts.value?.second?.optionGroups

        val optionNew = setProductOption(option, 1.000)
        val optionsGroupNew = if(optionsGroupOld == null){
            listOf(ProductOptionGroup(
                id = group.id,//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
                index = group.index,//0
                maximum = group.maximum,//2
                minimum = group.minimum,//0
                name = group.name,//"Adicionais"
                options = listOf(optionNew)
            ))
        } else {
            val groupListNew = mutableListOf<ProductOptionGroup>()

            for (groupOld in optionsGroupOld){
                if(groupOld?.id == group.id) {
                    groupListNew.add(ProductOptionGroup(
                        id = groupOld?.id,//"d67165ba-5b8e-4a46-ac4d-8ef55b6e0d9c"
                        index = groupOld?.index,//0
                        maximum = groupOld?.maximum,//2
                        minimum = groupOld?.minimum,//0
                        name = groupOld?.name,//"Adicionais"
                        options = listOf(optionNew)
                    ))
                } else {
                    groupOld?.let { groupListNew.add(it) }
                }
            }

            groupListNew
        }
        updateProductValue(optionsGroup = optionsGroupNew)
    }

}