package br.com.acbr.acbrselfservice.ui.product_list

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.databinding.FragmentMerchantMenuBinding
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.ui.product.ProductFragment
import br.com.acbr.acbrselfservice.util.AppBarStateChangedListener
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.ResourceStatus
import br.com.acbr.acbrselfservice.util.paging_progress_bar.PagingLoadStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MenuFragment : Fragment() {
    private var query: String? = null
    private var timerSearch = Timer()
    private val handler: Handler = Handler()
    private var fetchJob: Job? = null

    private var _binding: FragmentMerchantMenuBinding? = null
    private val binding get() = _binding!!
    private val menuViewModel: MenuViewModel by viewModel()
    private lateinit var adapter: ProductPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)

        query = savedInstanceState?.getString(LAST_SEARCH_QUERY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, query)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchantMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setResponseFetchProducts()
        setListener()
        fetchProducts(query)

        binding.toolbarLayoutMerchant.setExpandedTitleColor(resources.getColor(R.color.black))
        binding.toolbarLayoutMerchant.setCollapsedTitleTextColor(resources.getColor(R.color.white))

        showData()

        val screenHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val me = activity?.windowManager?.currentWindowMetrics
            me?.bounds?.height()
        } else {
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            dm.heightPixels
        }

        if(screenHeight != null && screenHeight > 0) {
            binding.rvProduct.layoutParams.height = screenHeight
        }

        binding.appBarMerchant.addOnOffsetChangedListener(object : AppBarStateChangedListener(){
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                //binding.toolbarMerchant.alpha = (appBarLayout?.totalScrollRange?:0 + 20).toFloat() / (appBarLayout?.totalScrollRange?:1)
                if(state == State.COLLAPSED){
                    binding.cvMerchantLogo.visibility = View.GONE
                    //binding.ibnBack.imageTintList = ColorStateList(arrayOf(intArrayOf(Color.WHITE)), intArrayOf(Color.WHITE))
                    binding.toolbarMerchant.navigationIcon?.setTint(Color.WHITE)
                } else {
                    binding.cvMerchantLogo.visibility = View.VISIBLE
                    //binding.ibnBack.imageTintList = ColorStateList(arrayOf(intArrayOf(Color.BLACK)), intArrayOf(Color.BLACK))
                    binding.toolbarMerchant.navigationIcon?.setTint(Color.BLACK)
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        fetchJob?.cancel()
        _binding = null
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                goBack()
            }
        }
        return false
    }*/

    private fun showData() {
//        it.backgroundImage?.let { backgroundImage -> showBackgroundImage(backgroundImage) }
//        it.logo?.let { logo -> showMerchantLogo(logo) }
//        it.name.let { name ->  binding.tvMerchantName.text = name}
//        it.isOpen?.let { isOpen ->  showIsOpenStatus(isOpen)}

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

    private fun showIsOpenStatus(isOpen: Boolean) {
        if (isOpen) {
            binding.tvIsOpenStatus.text = getString(R.string.merchant_is_open)
        } else {
            binding.tvIsOpenStatus.text = getString(R.string.merchant_is_closed)
        }
    }

    private fun setAdapter() {
        context?.let { cont ->
            adapter = ProductPagingAdapter(cont)
            adapter.setOnClickListener { gotoProduct(it) }
            binding.rvProduct.adapter = adapter.withLoadStateFooter(
                //header = MerchantMenuLoadStateAdapter{adapter.retry()},
                footer = PagingLoadStateAdapter{adapter.retry()}
            )
            /*binding.rvProduct.addItemDecoration(
                DividerItemDecoration(
                    binding.rvProduct.context,
                    DividerItemDecoration.VERTICAL
                )
            )*/

            adapter.addLoadStateListener { loadState ->
                proccessStatus(loadState)
            }
        }
    }

    private fun proccessStatus(loadState: CombinedLoadStates) {
        // show empty list
        if (loadState.refresh is LoadState.NotLoading) {
            if (adapter.itemCount == 0) {
                binding.textNoItem.text = getString(R.string.label_no_item)
                binding.textNoItem.visibility = View.VISIBLE
            } else {
                binding.textNoItem.visibility = View.GONE
            }
        } else if (loadState.refresh is LoadState.Loading) {
            binding.textNoItem.text = getString(R.string.loading)
            binding.textNoItem.visibility = View.VISIBLE
        } else {
            binding.textNoItem.visibility = View.GONE
        }


        // Only show the list if refresh succeeds.
        binding.rvProduct.isVisible = loadState.source.refresh is LoadState.NotLoading
        // Show loading spinner during initial load or refresh.
        binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        // Show the retry state if initial load or refresh fails.
        binding.btRetry.isVisible = loadState.source.refresh is LoadState.Error

        // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
            ?: loadState.source.refresh as? LoadState.Error
            ?: loadState.refresh as? LoadState.Error

        errorState?.let {
            val error = it.error.message
            if(error.toString().indexOf("400", 0, true) > -1){
                if(loadState.source.refresh is LoadState.Error){
                    binding.btRetry.isVisible = false
                    binding.textNoItem.text = getString(R.string.label_no_item)
                    binding.textNoItem.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireContext(), writeError(ResourceStatus(error?:"", ProcessStatus.Fail)), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchProducts(query: String?, retry: Boolean = false){
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            menuViewModel.productsFlow(
                //currentMerchant?.id ?: "",
                query, retry).collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun setResponseFetchProducts() {
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvProduct.scrollToPosition(0) }
        }
    }

    private fun setListener() {
        binding.toolbarMerchant.setNavigationOnClickListener {
            goBack()
        }

        binding.svItemSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(text: String?): Boolean {
                timerSearch.cancel()
                if(text.isNullOrBlank()){
                    query = null
                } else {
                    query = text
                }
                //binding.nsMain.smoothScrollTo(0, 600)app_bar_merchant
                binding.appBarMerchant.setExpanded(false)
                fetchProducts(query)
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                timerSearch.cancel()
                if(text.isNullOrBlank()){
                    query = null
                    //binding.nsMain.smoothScrollTo(0, 600)
                    binding.appBarMerchant.setExpanded(false)
                    fetchProducts(query)
                } else {
                    query = text
                    timerSearch = Timer()
                    timerSearch.schedule(
                        object : TimerTask() {
                            override fun run() {
                                handler.post {
                                    //binding.nsMain.smoothScrollTo(0, 600)
                                    if(_binding != null) {
                                        binding.appBarMerchant.setExpanded(false)
                                    }
                                    fetchProducts(query)
                                }
                            }
                        }, 1500)
                }
                return false
            }
        })

        binding.btRetry.setOnClickListener {
            fetchProducts(query, true)
        }
    }

    private fun gotoProduct(product: Product){
        val pos = Bundle().apply {
            putSerializable(ProductFragment.ARG_MODEL_PRODUCT, product)
        }
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_navigation_merchant_menu_to_navigation_product, pos)
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

    companion object {
        const val ARG_MERCHANT = "merchant"
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }

}