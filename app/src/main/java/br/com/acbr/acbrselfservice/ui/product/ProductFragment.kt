package br.com.acbr.acbrselfservice.ui.product

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.databinding.FragmentProductBinding
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.entity.ProductOption
import br.com.acbr.acbrselfservice.entity.ProductOptionGroup
import br.com.acbr.acbrselfservice.ui.cart.CartFragment
import br.com.acbr.acbrselfservice.ui.home.HomeActivity
import br.com.acbr.acbrselfservice.util.*
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductFragment : Fragment() {
    private var productCurrent: Product? = null
    private var editableProduct: Product? = null
    private val productViewModel: ProductViewModel by viewModel()
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private var quantityTotalCurrent: Double = 0.000
    private val loading = Loading()
    private var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            productCurrent = it.getSerializable(ARG_MODEL_PRODUCT) as? Product?
            editableProduct = it.getSerializable(ARG_EDITABLE_PRODUCT) as? Product?
            isUpdate = it.getBoolean(IS_UPDATE, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        addObservers()

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.toolbarProduct.setTitleTextColor(resources.getColor(R.color.black, resources.newTheme()))
        } else {*/
        binding.toolbarLayoutProduct.setExpandedTitleColor(resources.getColor(R.color.black))
        binding.toolbarLayoutProduct.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        //binding.toolbarLayoutProduct.setExpandedTitleTextColor(resources.getColor(R.color.black))
        //}
        /*binding.toolbarProduct.navigationIcon?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                colorFilter = BlendModeColorFilter(Color.BLACK, BlendMode.SRC_IN)
            }else{
                setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
            }
        }*/

        productViewModel.initEditableProduct(productCurrent, editableProduct)


        binding.appBarMerchant.addOnOffsetChangedListener(object : AppBarStateChangedListener(){
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                //toolbar_info.alpha = (appBarLayout.totalScrollRange + verticalOffset).toFloat() / appBarLayout.totalScrollRange
                if(state == State.COLLAPSED){
                    binding.toolbarProduct.navigationIcon?.setTint(Color.WHITE)
                } else {
                    binding.toolbarProduct.navigationIcon?.setTint(Color.BLACK)
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                goBack()
            }
        }
        return false
    }

    private fun setListener() {
        binding.toolbarProduct.setNavigationOnClickListener {
            goBack()
        }

        binding.btnDecreaseNumber.setOnClickListener {
            if(binding.tvProductNumber.text.isNotBlank()) {
                if (quantityTotalCurrent > 1) {
                    --quantityTotalCurrent
                    productViewModel.updateQuantity(quantityTotalCurrent)
                }
            }
        }
        binding.btnIncreaseNumber.setOnClickListener {
            if(binding.tvProductNumber.text.isNotBlank()) {
                quantityTotalCurrent++
                productViewModel.updateQuantity(quantityTotalCurrent)
            }
        }

        binding.edObservation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    productViewModel.updateObservation(it.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        if(isUpdate){
            binding.tvAddItem.setOnClickListener {
                productViewModel.updateProductInCart()
            }
            binding.tvTotalPrice.setOnClickListener {
                productViewModel.updateProductInCart()
            }
        } else {
            binding.tvAddItem.setOnClickListener {
                productViewModel.addProductInCart()
            }
            binding.tvTotalPrice.setOnClickListener {
                productViewModel.addProductInCart()
            }
        }
    }

    private fun addObservers() {
        productViewModel.workProducts.observe(viewLifecycleOwner) { products ->
            products?.let {
                showProductData(it.first, it.second)
                productViewModel.getProductPrice(it.first.uuid)
            }
        }

        productViewModel.quantity.observe(viewLifecycleOwner) { quantity ->
            quantity?.let {
                quantityTotalCurrent = quantity
                binding.tvProductNumber.text = Utils.toQuantityFormat(quantityTotalCurrent)
            }
        }

        productViewModel.productSend.observe(viewLifecycleOwner) { cart ->
            cart?.let {
                (activity as? HomeActivity)?.homeViewModel?.setCurrentCart(it)
                gotoCart(true)
            }
        }

        productViewModel.total.observe(viewLifecycleOwner) { total ->
            total?.let {
                binding.tvTotalPrice.text = Utils.toMonetarySymbolFormat(total)
            }
        }

        productViewModel.showProgress.observe(viewLifecycleOwner) { progress ->
            progress?.let {
                if (it) {
                    binding.progressBarHorizontal.visibility = View.VISIBLE
                } else {
                    binding.progressBarHorizontal.visibility = View.GONE
                }
            }
        }

        productViewModel.showBlockingProgress.observe(viewLifecycleOwner) { showProgress ->
            showProgress?.let {
                try {
                    if (it) {
                        loading.show(childFragmentManager, "loading")
                    } else {
                        loading.dismiss()
                    }
                } catch (e: Exception){}
            }
        }

        productViewModel.toastError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), writeError(it), Toast.LENGTH_SHORT).show()
                productViewModel.clearToastError()
            }
        }

        productViewModel.alertError.observe(viewLifecycleOwner) { error ->
            error?.let {
                if(error.status == ProcessStatus.MissingParameter){
                    UIFeedback.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.label_warning),
                        message = error.message,
                        isCancelable = false,
                        positiveListener = { _, _ ->
                            productViewModel.clearAlertError()
                        })
                } else {
                    UIFeedback.showCustomDialog(
                        context = requireContext(),
                        title = alertTitle(it),
                        message = writeError(it),
                        isCancelable = false,
                        positiveListener = { _, _ ->
                            productViewModel.clearAlertError()
                        })
                }
            }
        }
    }

    private fun showProductData(productModel: Product?, finishedProduct: Product?) {
        binding.llOptionGroup.removeAllViews()

        if(productModel != null) {
            binding.llCounter.visibility = View.VISIBLE
            binding.tvItemTitle.text = productModel.name
            binding.tvItemPrice.text = Utils.toMonetarySymbolFormat(productModel.value)
            binding.tvItemDescription.text = productModel.description
            binding.vItemDivider.visibility = View.VISIBLE

            binding.edObservation.setText(finishedProduct?.notes?:"")

            binding.textNoItem.text = getString(R.string.loading)
            binding.textNoItem.visibility = View.VISIBLE
            binding.progressBar.isVisible = true
            binding.btRetry.isVisible = false


            if(isUpdate){
                binding.tvAddItem.text = getString(R.string.label_update)
            } else {
                binding.tvAddItem.text = getString(R.string.label_add)
            }

            if(!productModel.imagePath.isNullOrBlank()){
                Picasso.get()
                    .load(productModel.imagePath)
                    .placeholder(R.drawable.ic_default_background)
                    .error(R.drawable.ic_default_background)
                    //.resize((50 * requireContext().resources.displayMetrics.density).toInt(), (50 * requireContext().resources.displayMetrics.density).toInt())
                    .centerInside()
                    .fit()
                    .into(binding.ivProductImage)
            }


            if (productModel.optionGroups != null) {
                val inflater = LayoutInflater.from(context)

                for (optionGroup in productModel.optionGroups) {
                    if (optionGroup != null) {
                        var finishedOptionGroup: ProductOptionGroup? = null
                        if(finishedProduct?.optionGroups != null) {
                            for (currentOptionGroup in finishedProduct.optionGroups) {
                                if(currentOptionGroup?.id == optionGroup.id){
                                    finishedOptionGroup = currentOptionGroup
                                }
                            }
                        }

                        val optionGroupView = makeOptionGroup(inflater, optionGroup, finishedOptionGroup)

                        binding.llOptionGroup.addView(optionGroupView)

                        binding.textNoItem.visibility = View.GONE
                        binding.progressBar.isVisible = false
                        binding.btRetry.isVisible = false
                    }
                }
            } else {
                binding.textNoItem.visibility = View.GONE
                binding.progressBar.isVisible = false
                binding.btRetry.isVisible = false
            }
        } else {
            binding.textNoItem.text = getString(R.string.label_no_item)
            binding.textNoItem.visibility = View.VISIBLE
            binding.progressBar.isVisible = false
            binding.btRetry.isVisible = false
        }
    }

    private fun makeOptionGroup(
        inflater: LayoutInflater,
        optionGroup: ProductOptionGroup,
        finishedOptionGroup: ProductOptionGroup?
    ): View? {
        val optionGroupView = inflater.inflate(R.layout.product_option_group, null)
        val title = optionGroupView.findViewById<TextView>(R.id.tv_checkbox_options_title)
        val quantitySubTitle = optionGroupView.findViewById<TextView>(R.id.tv_checkbox_options_quantity)
        val optionsContainer = optionGroupView.findViewById<LinearLayout>(R.id.ll_option_items)

        title.text = optionGroup.name
        var selecteds = 0.000
        if (!optionGroup.options.isNullOrEmpty()) {
            if (optionGroup.maximum?:0 > 1) {
                for (option in optionGroup.options) {
                    if (option != null) {
                        val itemView = makeOptionCounter(inflater, option, optionGroup, finishedOptionGroup)
                        selecteds += itemView.second
                        optionsContainer?.addView(itemView.first)
                    }
                }
            } else {
                for (option in optionGroup.options) {
                    if (option != null) {
                        val itemView = makeOptionRadio(inflater, option, optionGroup, finishedOptionGroup)
                        selecteds += itemView.second
                        optionsContainer?.addView(itemView.first)
                    }
                }
            }
        }
        quantitySubTitle.text = setCounterSelectedOption(optionGroup.minimum ?: 0, optionGroup.maximum ?: 0, selecteds)
        return optionGroupView
    }

    private fun makeOptionCounter(
        inflater: LayoutInflater,
        option: ProductOption,
        optionGroup: ProductOptionGroup,
        finishedOptionGroup: ProductOptionGroup?
    ): Pair<View?, Double> {
        val itemView = inflater.inflate(R.layout.product_counter_options_item, null)
        val itemTitle = itemView.findViewById<TextView>(R.id.tv_counter_options_item_title)
        val itemPrice = itemView.findViewById<TextView>(R.id.tv_counter_options_item_price)
        val tvCounter = itemView.findViewById<TextView>(R.id.tv_counter)
        val btMinus = itemView.findViewById<Button>(R.id.bt_minus)
        val btPlus = itemView.findViewById<Button>(R.id.bt_plus)
        var counter = 0.000

        if(finishedOptionGroup?.options != null) {
            for (finishedOption in finishedOptionGroup.options){
                if(finishedOption?.id == option.id){
                    //counter++
                    counter = finishedOption.amount?:1.000
                }
            }
        }

        itemTitle?.text = option.name
        itemPrice?.text = Utils.toMonetarySymbolFormat(option.value)
        tvCounter?.text = Utils.toQuantityFormat(counter)

        btPlus.setOnClickListener {
            productViewModel.addOption(option, optionGroup)
        }
        btMinus.setOnClickListener {
            productViewModel.removeOption(option, optionGroup)
        }
        return Pair(itemView, counter)
    }

    private fun makeOptionRadio(
        inflater: LayoutInflater,
        option: ProductOption,
        optionGroup: ProductOptionGroup,
        finishedOptionGroup: ProductOptionGroup?
    ): Pair<View?, Double> {
        val itemView = inflater.inflate(R.layout.radio_options_item, null)
        val itemTitle = itemView.findViewById<TextView>(R.id.tv_radio_options_item_title)
        val itemPrice = itemView.findViewById<TextView>(R.id.tv_radio_options_item_price)
        val radio = itemView.findViewById<RadioButton>(R.id.rb_item)

        val priceString = Utils.toMonetarySymbolFormat(option.value)
        itemTitle?.text = option.name
        itemPrice?.text = priceString
        var counter = 0.000

        if(finishedOptionGroup?.options != null) {
            for (finishedOption in finishedOptionGroup.options){
                if(finishedOption?.id == option.id){
                    radio.isChecked = true
                    //counter = 1
                    counter = finishedOption.amount?:1.000
                }
            }
        }

        radio.setOnClickListener {
            productViewModel.addRemoveOptionRadio(option, optionGroup)
        }

        return Pair(itemView, counter)
    }

    private fun setCounterSelectedOption(minimum: Int, maximum: Int, selecteds: Double): String{
        return if(maximum == 1){
            if(minimum > 0){
                "Obrigatória a escolha de um (${Utils.toQuantityFormat(selecteds)} / ${maximum})"
            } else {
                "Escolha um (${Utils.toQuantityFormat(selecteds)} / ${maximum})"
            }
        } else {
            if(minimum > 0){
                "Obrigatória a escolha de $minimum até $maximum (${Utils.toQuantityFormat(selecteds)} / ${maximum})"
            } else {
                "Escolha até $maximum (${Utils.toQuantityFormat(selecteds)} / ${maximum})"
            }
        }
    }


    private fun gotoCart(updatedCart: Boolean){
        val pos = Bundle().apply {
            putBoolean(CartFragment.ARG_UPDATED_CART, updatedCart)
        }
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_navigation_product_to_navigation_cart, pos)
    }

    private fun goBack(){
        NavHostFragment.findNavController(this).navigateUp()
    }

    private fun writeError(error: ResourceStatus): String {
        return when (error.status) {
            ProcessStatus.Success -> ""
            ProcessStatus.Fail -> error.message
            ProcessStatus.SaveFail -> getString(R.string.label_save_fail)
            ProcessStatus.FailExternalAPI -> error.message
            else -> error.message
        }
    }

    private fun alertTitle(error: ResourceStatus): String {
        return when (error.status) {
            ProcessStatus.Success -> getString(R.string.label_success)
            ProcessStatus.Fail -> getString(R.string.label_fail)
            ProcessStatus.SaveFail -> getString(R.string.label_fail)
            ProcessStatus.FailExternalAPI -> error.message
            else -> error.message
        }
    }

    companion object {
        const val ARG_MODEL_PRODUCT = "current_model_product"
        const val ARG_EDITABLE_PRODUCT = "current_editable_product"
        const val IS_UPDATE = "is_update"
    }
}