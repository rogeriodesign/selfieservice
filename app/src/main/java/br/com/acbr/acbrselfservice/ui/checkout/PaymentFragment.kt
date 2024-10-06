package br.com.acbr.acbrselfservice.ui.checkout

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import br.com.acbr.acbrselfservice.databinding.FragmentPaymentPixBinding
import br.com.acbr.acbrselfservice.ui.home.HomeActivity


class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentPixBinding? = null
    private val binding get() = _binding!!
    private val qrCodeToken = "https://github.com/g0dkar/qrcode-kotlin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentPixBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.vBtnCode.setOnClickListener {
            (activity as? HomeActivity)?.homeViewModel?.setPayment(qrCodeToken)
            goBack()
        }
    }

    private fun goBack(){
        NavHostFragment.findNavController(this).navigateUp()
    }

}