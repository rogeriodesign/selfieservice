package br.com.acbr.acbrselfservice.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.acbr.acbrselfservice.R

class Loading() : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return View.inflate(context, R.layout.loading_layout, null)
    }

    fun setIsCancelable(v: Boolean) {
        isCancelable = v
    }

    override fun onStart() {
        super.onStart()
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun show(manager: FragmentManager) {
        if(!isAdded) {
            show(manager, "loading...")
        }
    }
}