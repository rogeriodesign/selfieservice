package br.com.acbr.acbrselfservice.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import br.com.acbr.acbrselfservice.R
import com.google.android.material.snackbar.Snackbar
import com.rommansabbir.networkx.NetworkXProvider


open class BaseActivity : AppCompatActivity() {
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var snack: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //window.statusBarColor = ContextCompat.getColor(this, R.color.background_primary)

        inputMethodManager =  this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        setSnack()
    }

    override fun onResume() {
        super.onResume()
        detectInternet()
    }


    private fun detectInternet (){
        val status = NetworkXProvider.isInternetConnected
        if(status && snack.isShown){
            snack.dismiss()
        } else {
            snack.show()
        }

        NetworkXProvider.isInternetConnectedLiveData.observe(this) { status ->
            status?.let {
                if(it){
                    snack.dismiss()
                } else {
                    snack.show()
                }
            }
        }
    }

    fun isInternetConnected (): Boolean {
        return NetworkXProvider.isInternetConnected
    }

    private fun setSnack(){
        snack = Snackbar.make(window.decorView, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
            //.setAnchorView() BottomNavigationView(this))
            .setBackgroundTint(resources.getColor(R.color.background_warning))
            //.setTextColor()
        // As linhas abaixo servem para manter o snack alinhado no topo da tela
        val view: View = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        // calculate actionbar height
        val tv = TypedValue()
        var actionBarHeight = 0
        if (theme.resolveAttribute(androidx.appcompat.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }

        // set margin
        params.setMargins(0, actionBarHeight+100, 0, 0)
        view.layoutParams = params
    }

    fun showKeyboard(editText: EditText){
        Handler().postDelayed({
            editText.requestFocus()
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }, 200L)
    }

    fun hideKeyboard(){
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}