package br.com.acbr.acbrselfservice.repository.configuration

import android.content.Context
import android.content.SharedPreferences
import br.com.acbr.acbrselfservice.entity.ConfigurationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigurationRepository (private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Main)

    fun saveServerAddress(
        address: String,
        onPostExecute: ((Boolean) -> Unit)? = null
    ) {
        scope.launch {
            val response = saveServerAddressSync(address)
            onPostExecute?.let {
                it(response)
            }
        }
    }

    fun getServerAddress(
        onPostExecute: (String?) -> Unit
    ) {
        scope.launch {
            val value = getServerAddressSync()
            onPostExecute(value)
        }
    }

    fun saveConfiguration(
        configuration: ConfigurationData,
        onPostExecute: ((Boolean) -> Unit)? = null
    ) {
        scope.launch {
            val response = saveConfigurationSync(configuration)
            onPostExecute?.let {
                it(response)
            }
        }
    }

    fun getConfiguration(
        onPostExecute: (ConfigurationData) -> Unit
    ) {
        scope.launch {
            val value = getConfigurationSync()
            onPostExecute(value)
        }
    }



    private suspend fun saveServerAddressSync(address: String) = withContext(Dispatchers.IO) {
        // Precisamos de um objeto Editor para fazer mudanças de preferência.
        // Todos os objetos são de android.context.Context
        val settings: SharedPreferences = context.getSharedPreferences(CONFIGURATION, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putString(SERVER_ADDRESS, address)

        // Commit as edições
        editor.apply()

        true
    }

    private suspend fun getServerAddressSync() = withContext(Dispatchers.IO) {
        val settings: SharedPreferences = context.getSharedPreferences(CONFIGURATION, Context.MODE_PRIVATE)
        val value = settings.getString(SERVER_ADDRESS, null)
        value
    }

    private suspend fun saveConfigurationSync(configuration: ConfigurationData) = withContext(Dispatchers.IO) {
        // Precisamos de um objeto Editor para fazer mudanças de preferência.
        // Todos os objetos são de android.context.Context
        val settings: SharedPreferences = context.getSharedPreferences(CONFIGURATION, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putString(MERCHANT_LOGO, configuration.logo)
        editor.putString(HEAD_IMAGE, configuration.headImage)

        // Commit as edições
        editor.apply()

        true
    }

    private suspend fun getConfigurationSync() = withContext(Dispatchers.IO) {
        val settings: SharedPreferences = context.getSharedPreferences(CONFIGURATION, Context.MODE_PRIVATE)
        ConfigurationData(
            logo = settings.getString(MERCHANT_LOGO, null),
            headImage = settings.getString(HEAD_IMAGE, null)
        )
    }


    companion object {
        private const val CONFIGURATION = "configuration"
        private const val SERVER_ADDRESS = "serverAddress"
        private const val MERCHANT_LOGO = "MERCHANT_LOGO"
        private const val HEAD_IMAGE = "HEAD_IMAGE"
    }
}