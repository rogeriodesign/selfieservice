package br.com.acbr.acbrselfservice.ui.checkout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.databinding.FragmentOrderBinding
import br.com.acbr.acbrselfservice.entity.Order
import br.com.acbr.acbrselfservice.util.Utils
import com.squareup.picasso.Picasso

class OrderFragment : Fragment() {
    private var orderCurrent: Order? = null
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            orderCurrent = it.getSerializable(ARG_ORDER_CURRENT) as? Order?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeView(orderCurrent)
        setListener()
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
        /*binding
        btDetails.setOnClickListener {
            onClickListenerGoto(order)
        }*/
    }

    private fun makeView(order: Order?) {
        binding.tvDeliveryFeeTitle.visibility = View.GONE
        binding.tvDeliveryFeeValue.visibility = View.GONE
        binding.tvDiscountCouponTitle.visibility = View.GONE
        binding.tvDiscountCouponValue.visibility = View.GONE

        binding.tvPayment.text = "Cartão de crédito"

        if (order != null && !order.products.isNullOrEmpty()) {
            binding.llContainerMerchant.removeAllViews()
            Log.i("order", "merchants - "+ order.products.size)
            var orderValueTotal = 0.000
            for (product in order.products){
                if(product != null){
                    val inflater = LayoutInflater.from(context)

                    var productValueTotal = 0.000
                    Log.i("order", "produto - "+ product.name)
                    val containerProductView = inflater.inflate(R.layout.order_detail_container_product, null)
                    val tvProductNumber: TextView = containerProductView.findViewById(R.id.tv_number)
                    val tvProductDescription: TextView = containerProductView.findViewById(R.id.tv_description)
                    val tvValue: TextView = containerProductView.findViewById(R.id.tv_value)

                    tvProductNumber.text = "Nº"
                    var description = product.name

                    if(!product.optionGroups.isNullOrEmpty()){
                        for (optionGroup in product.optionGroups){
                            if(optionGroup != null){
                                if(!optionGroup.options.isNullOrEmpty()) {
                                    for (option in optionGroup.options) {
                                        description += (" + ${Utils.toQuantityFormat(option?.amount)} ${option?.name}")
                                        productValueTotal += (((option?.value?:0.000) * (option?.amount?:1.000)))
                                    }
                                } else {
                                    description += (" + ${optionGroup.name?:""}")
                                    Log.i("order", "option group - "+ optionGroup.name)
                                }
                            }
                        }
                    }
                    productValueTotal += product.value?:0.000
                    productValueTotal *= (product.amount ?: 1.000)
                    tvProductDescription.text = description
                    tvValue.text = Utils.toMonetarySymbolFormat(productValueTotal)

                    orderValueTotal += productValueTotal
                    binding.llContainerMerchant.addView(containerProductView)
                }
            }
            binding.tvTotalValue.text = Utils.toMonetarySymbolFormat(orderValueTotal)
            binding.tvSubtotalValue.text = Utils.toMonetarySymbolFormat(orderValueTotal)
        }
    }

    private fun goBack(){
        NavHostFragment.findNavController(this).navigateUp()
    }

    companion object {
        const val ARG_ORDER_CURRENT = "order_current"
    }
}