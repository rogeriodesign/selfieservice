package br.com.acbr.acbrselfservice.ui.splash

import android.content.ContentResolver
import androidx.lifecycle.ViewModel

class SplashViewModel (private val useCase: SplashContract.UseCase) : ViewModel(), SplashContract.Controller {

    override fun saveDeviceUniqueId (resolver: ContentResolver){
        useCase.saveDeviceUniqueId(resolver){}
    }
}