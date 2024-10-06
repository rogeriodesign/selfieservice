package br.com.acbr.acbrselfservice

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import br.com.acbr.acbrselfservice.di.DependencyModule
import com.rommansabbir.networkx.NetworkXConfig
import com.rommansabbir.networkx.NetworkXProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        if (instance == null) {
            instance = this
        }

        val builder = NetworkXConfig.Builder()
            .withApplication(this)
            // Você pode desativar o medidor de velocidade se não for necessário
            //.withEnableSpeedMeter(true)
            .build()
        NetworkXProvider.enable(builder)

        startKoin {
            //androidLogger()
            androidContext(this@MyApplication)
            modules(DependencyModule.moduleApp)

            //Para androidx.fragment, desde a versão 2.1.0-alpha-3, a FragmentFactory foi introduzida para criar uma instância da classe Fragment.
        // No início da declaração KoinApplication, use a palavra-chave fragmentFactory () para configurar a instância KoinFragmentFactory padrão.
            /*androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
            androidFileProperties()
            // setup a KoinFragmentFactory instance
            fragmentFactory()

            modules(...)*/
        }
    }

    companion object{
        private var instance: MyApplication? = null
        fun getInstance(): MyApplication =
            if (instance == null) {
                instance = MyApplication()
                instance!!
            } else {
                instance!!
            }

        fun hasNetwork(): Boolean = isNetworkConnected()

        private fun isNetworkConnected(): Boolean {
            val cm: ConnectivityManager? = instance?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

            val activeNetwork: NetworkInfo? = cm?.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }
}