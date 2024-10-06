package br.com.acbr.acbrselfservice.ui.checkout.resume

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.NavHostFragment
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.databinding.FragmentResumeBinding
import br.com.acbr.acbrselfservice.entity.Cart
import br.com.acbr.acbrselfservice.entity.Order
import br.com.acbr.acbrselfservice.ui.checkout.OrderFragment
import br.com.acbr.acbrselfservice.ui.home.HomeActivity
import br.com.acbr.acbrselfservice.util.*
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResumeFragment : Fragment() {
    private var tableCodeCurrent: String? = null
    private var _binding: FragmentResumeBinding? = null
    private val binding get() = _binding!!
    private val loading = Loading()
    private val resumeViewModel: ResumeViewModel by viewModel()
    private var cartIdCurrent: String? = null
    private var orderCurrent: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            tableCodeCurrent = it.getString(ARG_TABLE_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        addObservers()
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
        binding.btAddOrder.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_resume_to_navigation_payment_pix)
        }

        binding.btSuccess.setOnClickListener {
            val arg = Bundle().apply {
                putSerializable(OrderFragment.ARG_ORDER_CURRENT, orderCurrent)
            }
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_resume_to_navigation_order, arg)
        }
    }

    private fun addObservers(){
        (activity as? HomeActivity)?.homeViewModel?.cart?.observe(viewLifecycleOwner) { cart ->
            if(cart != null) {
                binding.textNoItem.visibility = View.GONE
                showCartData(cart)
                cartIdCurrent = ""
            } else {
                binding.textNoItem.text = getString(R.string.label_no_item)
                binding.textNoItem.visibility = View.VISIBLE
            }
        }

        (activity as? HomeActivity)?.homeViewModel?.paymentToken?.observe(viewLifecycleOwner) { token ->
            token?.let {
                resumeViewModel.postOrder(cartIdCurrent?:"", it, "TAKE_AWAY")
            }
        }

        resumeViewModel.order.observe(viewLifecycleOwner) { order ->
            order?.let {
                orderCurrent = it
                binding.llSuccess.visibility = View.VISIBLE
            }
        }

        resumeViewModel.showBlockingProgress.observe(viewLifecycleOwner) { progress ->
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

        resumeViewModel.toastError.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.textNoItem.text = getString(R.string.label_no_item)
                Toast.makeText(requireContext(), writeError(it), Toast.LENGTH_SHORT).show()
                resumeViewModel.clearToastError()
            }
        }

        resumeViewModel.alertError.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.textNoItem.text = getString(R.string.label_no_item)
                UIFeedback.showCustomDialog(
                    context = requireContext(),
                    title = alertTitle(it),
                    message = writeError(it),
                    isCancelable = false,
                    positiveListener = DialogInterface.OnClickListener { _, _ ->
                        resumeViewModel.clearAlertError()
                    })
            }
        }
    }

    private fun showCartData(cart: Cart){
        binding.llContainerMain.removeAllViews()

        binding.clMain.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.textNoItem.visibility = View.VISIBLE
        binding.textNoItem.text = getString(R.string.loading)

        binding.tvTotalValue.text = Utils.toMonetarySymbolFormat(cart.total)
        binding.tvSubtotalValue.text = Utils.toMonetarySymbolFormat(cart.total)

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

                    btProductEdit.visibility = View.GONE
                    btProductRemove.visibility = View.GONE

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
            binding.textNoItem.visibility = View.GONE
        } else {
            binding.clMain.visibility = View.GONE
            binding.textNoItem.visibility = View.VISIBLE
            binding.textNoItem.text = getString(R.string.label_no_item)
        }

        binding.progressBar.visibility = View.GONE
    }

    private fun writeError(status: ResourceStatus): String{
        return when (status.status){
            ProcessStatus.Success -> ""
            ProcessStatus.Fail -> status.message
            ProcessStatus.SaveFail -> getString(R.string.label_save_fail)
            ProcessStatus.FailExternalAPI ->  status.message
            ProcessStatus.MissingParameter -> "Uma forma de pagamento é necessária"
            else ->  status.message
        }
    }

    private fun alertTitle(status: ResourceStatus): String{
        return when (status.status){
            ProcessStatus.Success -> getString(R.string.label_success)
            ProcessStatus.Fail -> getString(R.string.label_fail)
            ProcessStatus.SaveFail -> getString(R.string.label_fail)
            ProcessStatus.FailExternalAPI ->  status.message
            ProcessStatus.MissingParameter -> getString(R.string.label_warning)
            else ->  status.message
        }
    }

    private fun goBack(){
        NavHostFragment.findNavController(this).navigateUp()
    }

    companion object {
        const val ARG_TABLE_CODE = "table_code"
    }
}