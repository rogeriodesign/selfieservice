package br.com.acbr.acbrselfservice.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory (private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        /*if(modelClass.isAssignableFrom(LoginEmailViewModel::class.java)){
            val authenticationService = AuthenticationService()

            val oauthRepository = OauthRepository(context)
            val configurationRepository = ConfigurationRepository(context)
            val authenticationRepository = AuthenticationRepository(authenticationService)

            val useCase = LoginUseCase(configurationRepository, oauthRepository, authenticationRepository)
            return LoginEmailViewModel(useCase) as T
        }*/

        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}