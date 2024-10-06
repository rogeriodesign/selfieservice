package br.com.acbr.acbrselfservice.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.databinding.ActivityHomeBinding
import br.com.acbr.acbrselfservice.ui.BaseActivity
import br.com.acbr.acbrselfservice.ui.cart.CartFragment
import br.com.acbr.acbrselfservice.util.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private var canShowNavigationCart = false
    val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        canShowNavigationCart = savedInstanceState?.getBoolean(CAN_SHOW_NAVIGATION_CART, false) ?: false

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar_layout))

        navController = findNavController(R.id.nav_host_fragment_activity_home)


        // o listener abaixo é necessário porque por algum motivo quando se troca a toolbar, como é feito no cardápio,
        // o titulo não mudava quando se navegava para a página seguinte ou para a anterior
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = destination.label
            binding.actionBar.toolbarLayout.title = title

            // o método abaixo serve para incluir ou remover a navegação do carrinho no rodapé segundo o fragment carregado
            when (destination.id) {
                R.id.navigation_merchant_menu -> {
                    canShowNavigationCart = true
                    showNavigationCart()
                }
                else -> {
                    canShowNavigationCart = false
                    hideNavigationCart()
                }
            }

            when (destination.id) {
                R.id.navigation_merchant_menu -> {
                    binding.actionBar.toolbarLayout.visibility = View.GONE
                }
                R.id.navigation_product -> {
                    binding.actionBar.toolbarLayout.visibility = View.GONE
                }
                else -> {
                    binding.actionBar.toolbarLayout.visibility = View.VISIBLE
                }
            }
        }

        initCurrentData()

        binding.navViewCart.setOnClickListener {
            gotoCart()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CAN_SHOW_NAVIGATION_CART, canShowNavigationCart)
    }

    private fun initCurrentData() {
        hideNavigationCart()
        homeViewModel.getCart()
        homeViewModel.cart.observe(this) { cart ->
            if(cart != null && !cart.products.isNullOrEmpty()) {
                showNavigationCart()
                binding.clContent.visibility = View.VISIBLE
                binding.tvTotal.text = Utils.toMonetarySymbolFormat(cart.total)
                var quantity = 0.000
                for (product in cart.products) {
                    quantity += product?.amount?:0.000
                }
                binding.tvQuantity.text = Utils.toQuantityFormat(quantity)
            } else {
                hideNavigationCart()
            }
        }
    }


    private fun showNavigationCart(){
        if(canShowNavigationCart && homeViewModel.cart.value != null && !homeViewModel.cart.value!!.products.isNullOrEmpty() ) {
            binding.navViewCart.visibility = View.VISIBLE
        }
    }

    private fun hideNavigationCart(){
        binding.navViewCart.visibility = View.GONE
    }

    private fun gotoCart(){
        val fragment = CartFragment()
        val bundle = Bundle().apply {
            putBoolean(CartFragment.ARG_UPDATED_CART, false)
        }
        fragment.arguments = bundle
        /*this.supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_home, fragment, "TAG_CART_FRAGMENT")
            .addToBackStack(null)
            .commit()*/

        val pos = Bundle().apply {
            putBoolean(CartFragment.ARG_UPDATED_CART, false)
        }
        /*NavHostFragment.findNavController(fragment)
            .navigate(R.id.action_navigation_product_to_navigation_cart, pos)*/

        navController.navigate(R.id.navigation_cart, pos)
    }

    /*override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home)?.childFragmentManager?.fragments?.get(0)
        (fragment as? IShowBottomNavigationOnBack)?.showBottomNavigationOnBack()?.let {
            if(it){
                showNavigation()
            } else {
                hideNavigation()
            }
        }
        super.onBackPressed()
    }*/



    companion object {
        private const val CAN_SHOW_NAVIGATION_CART = "can_show_navigation_cart"
        fun getIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}