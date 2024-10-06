package br.com.acbr.acbrselfservice.ui.cart

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.databinding.FragmentCartBinding
import br.com.acbr.acbrselfservice.entity.Cart
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.ui.home.HomeActivity
import br.com.acbr.acbrselfservice.ui.product.ProductFragment
import br.com.acbr.acbrselfservice.util.*
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CartFragment : Fragment() {

    private val cartViewModel: CartViewModel by viewModel()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private var timerCoupon = Timer()
    private val handler: Handler = Handler()
    private var updatedCart: Boolean = false
    private val loading = Loading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            updatedCart = it.getBoolean(ARG_UPDATED_CART, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        addObservers()
        if(!updatedCart){
            cartViewModel.getCart()
        }
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

    private fun setListener(){
        binding.rlTop.setOnClickListener {
            binding.cvTopOpen.visibility = View.VISIBLE
        }
        binding.tvHowWantReceive.setOnClickListener {
            binding.cvTopOpen.visibility = View.GONE
        }
        binding.edCouponDiscount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                timerCoupon.cancel()
                if(text.isNullOrBlank()){
                    cartViewModel.getCoupon(null)
                } else {
                    timerCoupon = Timer()
                    timerCoupon.schedule(
                        object : TimerTask() {
                            override fun run() {
                                handler.post {
                                    cartViewModel.getCoupon(text.toString())
                                }
                            }
                        }, 1500)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.btAddProduct.setOnClickListener {
            gotoAddProduct()
        }

        binding.cbOptionReceive.setOnCheckedChangeListener { compoundButton, isChecked ->
            binding.cbOptionRemove.isChecked = !isChecked
            setReceive(binding.cbOptionReceive.isChecked)
        }

        binding.cbOptionRemove.setOnCheckedChangeListener { compoundButton, isChecked ->
            binding.cbOptionReceive.isChecked = !isChecked
            setReceive(binding.cbOptionReceive.isChecked)
        }

        binding.tvNext.setOnClickListener {
            /*NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_cart_to_navigation_removal)*/
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_cart_to_navigation_resume)
        }
    }

    private fun addObservers(){
        cartViewModel.cart.observe(viewLifecycleOwner) { cart ->
            if(cart != null) {
                (activity as? HomeActivity)?.homeViewModel?.setCurrentCart(cart)
            }
        }

        (activity as? HomeActivity)?.homeViewModel?.cart?.observe(viewLifecycleOwner) { cart ->
            if(cart != null) {
                binding.textNoItem.visibility = View.GONE
                showCartData(cart)
            } else {
                binding.textNoItem.text = getString(R.string.label_no_item)
                binding.textNoItem.visibility = View.VISIBLE
            }
        }

        cartViewModel.showProgress.observe(viewLifecycleOwner) { progress ->
            progress?.let {
                if(it){
                    binding.clMain.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textNoItem.visibility = View.VISIBLE
                    binding.textNoItem.text = getString(R.string.loading)
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        cartViewModel.showBlockingProgress.observe(viewLifecycleOwner) { progress ->
            progress?.let {
                try {
                    if (it) {
                        loading.show(childFragmentManager, "loading")
                    } else {
                        loading.dismiss()
                    }
                } catch (e: Exception){}
            }
        }

        cartViewModel.toastError.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.textNoItem.text = getString(R.string.label_no_item)
                Toast.makeText(requireContext(), writeError(it), Toast.LENGTH_SHORT).show()
                cartViewModel.clearToastError()
            }
        }

        cartViewModel.alertError.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.textNoItem.text = getString(R.string.label_no_item)
                UIFeedback.showCustomDialog(
                    context = requireContext(),
                    title = alertTitle(it),
                    message = writeError(it),
                    isCancelable = false,
                    positiveListener = DialogInterface.OnClickListener { _, _ ->
                        cartViewModel.clearAlertError()
                    })
            }
        }

        cartViewModel.alertConfirmRemove.observe(viewLifecycleOwner) { product ->
            product?.let {
                UIFeedback.showCustomDialog(
                    context = requireContext(),
                    title = getString(R.string.label_warning),
                    message = getString(R.string.confirm_exclusion, product.first.name),
                    isCancelable = false,
                    buttonTitlePositive = getString(R.string.label_yes),
                    buttonTitleCancel = getString(R.string.label_not),
                    positiveListener = { _, _ ->
                        cartViewModel.clearAlertConfirmRemove()
                        cartViewModel.removeProduct(product.first)
                    },
                    cancelListener = { _, _ ->
                        cartViewModel.clearAlertConfirmRemove()
                    })
            }
        }
    }

    private fun showCartData(cart: Cart){
        binding.llContainerMain.removeAllViews()

        binding.tvTotalValue.text = Utils.toMonetarySymbolFormat(cart.total)
        binding.tvSubtotalValue.text = Utils.toMonetarySymbolFormat(cart.total)

        binding.tvTotalFooterValue.text = Utils.toMonetarySymbolFormat(cart.total)

        binding.tvPreparationTime.text = ("15/20 min")
        binding.tvOptionReceiveTime.text = requireContext().getString(R.string.label_standard_delivery, " Hoje, 50/60 min - R\$7,90")
        binding.tvOptionRemoveAddress.text = ("Rua das Maravilhas, 123 - Vila Paraíso")

        binding.tvDeliveryFeeTitle.visibility = View.GONE
        binding.tvDeliveryFeeValue.visibility = View.GONE
        binding.tvDiscountCouponTitle.visibility = View.GONE
        binding.tvDiscountCouponValue.visibility = View.GONE

        if(!cart.products.isNullOrEmpty()){
            for (product in cart.products){
                if(product != null){
                    val inflater = LayoutInflater.from(context)
                    var valueTotal = 0.000
                    val containerProductView = inflater.inflate(R.layout.cart_container_product, null)
                    val tvProductTitle: TextView = containerProductView.findViewById(R.id.tv_title)
                    val tvProductTotal: TextView = containerProductView.findViewById(R.id.tv_total_value)
                    val tvQuantity: TextView = containerProductView.findViewById(R.id.tv_quantity)
                    val btProductEdit: Button = containerProductView.findViewById(R.id.bt_edit)
                    val btProductRemove: Button = containerProductView.findViewById(R.id.bt_remove)
                    tvProductTitle.text = product.name
                    tvQuantity.text = Utils.toQuantityFormat(product.amount)


                    btProductEdit.setOnClickListener {
                        editProduct(product)
                    }

                    btProductRemove.setOnClickListener {
                        confirmRemoveProduct(product)
                    }

                    if(!product.imagePath.isNullOrBlank()){
                        val ivProductImage: ImageView = containerProductView.findViewById(R.id.iv_image)
                        Picasso.get()
                            .load(product.imagePath)
                            .placeholder(R.drawable.ic_default_background)
                            .error(R.drawable.ic_default_background)
                            //.resize((50 * requireContext().resources.displayMetrics.density).toInt(), (50 * requireContext().resources.displayMetrics.density).toInt())
                            .centerInside()
                            .fit()
                            .into(ivProductImage)
                    }

                    if(!product.optionGroups.isNullOrEmpty()){
                        val containerProductOptions: LinearLayout = containerProductView.findViewById(R.id.ll_options)

                        for (optionGroup in product.optionGroups){

                            if(optionGroup != null){
                                if(!optionGroup.options.isNullOrEmpty()) {
                                    for (option in optionGroup.options) {
                                        val containerOptionView = inflater.inflate(R.layout.cart_product_option, null)
                                        val tvOprionName: TextView = containerOptionView.findViewById(R.id.tv_option_name)
                                        //tvOprionName.text = if(optionGroup.name.isNullOrBlank()) option?.name else optionGroup.name+": ("+Utils.toQuantityFormat(option?.amount)+") "+option?.name
                                        tvOprionName.text = ("${Utils.toQuantityFormat(option?.amount)} - ${option?.name}")
                                        containerProductOptions.addView(containerOptionView)

                                        valueTotal += (((option?.value?:0.000) * (option?.amount?:1.000)))
                                    }
                                } else {
                                    val containerOptionView = inflater.inflate(R.layout.cart_product_option, null)
                                    val tvOprionName: TextView = containerOptionView.findViewById(R.id.tv_option_name)
                                    tvOprionName.text = optionGroup.name?:""
                                    containerProductOptions.addView(containerOptionView)
                                }
                            }
                        }
                    }
                    valueTotal += product.value?:0.000

                    valueTotal *= (product.amount ?: 1.000)
                    tvProductTotal.text = Utils.toMonetarySymbolFormat(valueTotal)



                    binding.llContainerMain.addView(containerProductView)
                }
            }
            binding.clMain.visibility = View.VISIBLE
        } else {
            binding.clMain.visibility = View.GONE
            binding.textNoItem.visibility = View.VISIBLE
            binding.textNoItem.text = getString(R.string.label_no_item)
        }
    }

    private fun setReceive(receive: Boolean){
        if(receive){
            binding.tvDeliveryFeeValue.text = Utils.toMonetarySymbolFormat(7.900)
            binding.tvDeliveryFeeTitle.visibility = View.VISIBLE
            binding.tvDeliveryFeeValue.visibility = View.VISIBLE

            val total = (binding.tvSubtotalValue.text.toString().replace("R$", "").replace(",", ".").trim()).toDouble()
            binding.tvTotalValue.text = Utils.toMonetarySymbolFormat(total + 7.900)
            binding.tvTotalFooterValue.text = Utils.toMonetarySymbolFormat(total + 7.900)
        } else {
            binding.tvDeliveryFeeTitle.visibility = View.GONE
            binding.tvDeliveryFeeValue.visibility = View.GONE

            val total = (binding.tvSubtotalValue.text.toString().replace("R$", "").replace(",", ".").trim()).toDouble()
            binding.tvTotalValue.text = Utils.toMonetarySymbolFormat(total)
            binding.tvTotalFooterValue.text = Utils.toMonetarySymbolFormat(total)
        }
    }

    private fun editProduct(product: Product){
        val pos = Bundle().apply {
            putSerializable(ProductFragment.ARG_EDITABLE_PRODUCT, product)
            putBoolean(ProductFragment.IS_UPDATE, true)
        }
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_navigation_cart_to_navigation_product, pos)
    }

    private fun confirmRemoveProduct(product: Product){
        cartViewModel.confirmRemoveProduct(product)
    }

    private fun goBack(){
        NavHostFragment.findNavController(this).navigateUp()
    }

    private fun gotoAddProduct(){
        NavHostFragment.findNavController(this).navigateUp()
    }

    private fun writeError(error: ResourceStatus): String{
        return when (error.status){
            ProcessStatus.Success -> ""
            ProcessStatus.Fail -> error.message
            ProcessStatus.SaveFail -> getString(R.string.label_save_fail)
            ProcessStatus.FailExternalAPI ->  error.message
            else ->  error.message
        }
    }

    private fun alertTitle(error: ResourceStatus): String{
        return when (error.status){
            ProcessStatus.Success -> getString(R.string.label_success)
            ProcessStatus.Fail -> getString(R.string.label_fail)
            ProcessStatus.SaveFail -> getString(R.string.label_fail)
            ProcessStatus.FailExternalAPI ->  error.message
            else ->  error.message
        }
    }

    companion object {
        const val ARG_UPDATED_CART = "current_cart"
    }
}