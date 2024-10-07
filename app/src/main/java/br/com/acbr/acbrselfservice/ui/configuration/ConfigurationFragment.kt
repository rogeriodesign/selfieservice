package br.com.acbr.acbrselfservice.ui.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.databinding.FragmentConfigurationBinding
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.ResourceStatus
import br.com.acbr.acbrselfservice.util.UIFeedback
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationFragment : Fragment() {
    private val configurationViewModel: ConfigurationViewModel by viewModel()

    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        addObservers()
        configurationViewModel.updateProfile()
        showData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        configurationViewModel.saveServerAddress(binding.tiIp.text.toString())
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

    private fun addObservers(){
        configurationViewModel.serverAddress.observe(viewLifecycleOwner){
            it?.let {
                binding.tiIp.setText(it)
            }
        }

        configurationViewModel.configuration.observe(viewLifecycleOwner) {
            it?.let {
                it.headImage?.let { backgroundImage -> showBackgroundImage(backgroundImage) }
                it.logo?.let { logo -> showMerchantLogo(logo) }
            }
        }

        configurationViewModel.showProgress.observe(viewLifecycleOwner, Observer { progress ->
            progress?.let {
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        configurationViewModel.toastError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(requireContext(), writeError(it), Toast.LENGTH_SHORT).show()
                configurationViewModel.clearToastError()
            }
        })

        configurationViewModel.alertError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                UIFeedback.showCustomDialog(
                    context = requireContext(),
                    title = alertTitle(it),
                    message = writeError(it),
                    isCancelable = false,
                    positiveListener = { _, _ ->
                        configurationViewModel.clearAlertError()
                    })
            }
        })
    }

    private fun showData() {


    }

    private fun showBackgroundImage(uri: String) {
        Picasso.get()
            .load(uri)
            .placeholder(R.drawable.ic_default_background)
            .error(R.drawable.ic_default_background)
            .resize((50 * requireContext().resources.displayMetrics.density).toInt(), (50 * requireContext().resources.displayMetrics.density).toInt())
            .centerInside()
            //.fit()
            .into(binding.ivBackgroundImage)
    }

    private fun showMerchantLogo(uri: String) {
        Picasso.get()
            .load(uri)
            .placeholder(R.drawable.ic_default_background)
            .error(R.drawable.ic_default_background)
            .resize((50 * requireContext().resources.displayMetrics.density).toInt(), (50 * requireContext().resources.displayMetrics.density).toInt())
            .centerInside()
            //.fit()
            .into(binding.ivMerchantLogo)
    }

    private fun setListener(){

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

    private fun goBack(){
        NavHostFragment.findNavController(this).navigateUp()
    }
}