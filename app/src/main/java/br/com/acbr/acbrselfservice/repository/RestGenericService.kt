package br.com.acbr.acbrselfservice.repository

import android.util.Log
import br.com.acbr.acbrselfservice.MyApplication
import br.com.acbr.acbrselfservice.repository.store_data.service.IStoreDataService
import br.com.acbr.acbrselfservice.repository.cart.service.ICartService
import br.com.acbr.acbrselfservice.repository.order.service.IOrderService
import br.com.acbr.acbrselfservice.repository.product.service.IProductService
import br.com.acbr.acbrselfservice.repository.profile.service.IMeService
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class RestGenericService (private val timeCache: Int = 300, private val timeOut: Long? = null) {



    fun getStoreDataService(rote: String): IStoreDataService {
        return restInit(rote, timeCache, timeOut).create(IStoreDataService::class.java)
    }

    fun getMeService(): IMeService {
        return restInit(ExternalRoutes.customer, timeCache, timeOut).create(IMeService::class.java)
    }

    fun getProductService(): IProductService {
        return restInit(ExternalRoutes.merchant, timeCache, timeOut).create(IProductService::class.java)
    }

    fun getCartService(): ICartService {
        return restInit(ExternalRoutes.order, timeCache, timeOut).create(ICartService::class.java)
    }

    fun getOrderService(): IOrderService {
        return restInit(ExternalRoutes.order, timeCache, timeOut).create(IOrderService::class.java)
    }

    companion object{
        private const val cacheSize: Long = 5 * 1024 * 1024 // 5 MB

        private fun cache(): Cache {
            return Cache(File(MyApplication.getInstance().cacheDir, "someIdentifier"), cacheSize)
        }

        private fun restInit(url: String, timeCache: Int = 120, timeOut: Long? = null): Retrofit {//BuildConfig.CUSTOMER_API_URL
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor(interceptor)
                //This interceptor will be called ONLY if the network is available
                .addNetworkInterceptor (networkInterceptor(timeCache))//online
                //This interceptor will be called both if the network is available and if the network is not available
                .addInterceptor(offlineInterceptor())
                .connectTimeout(timeOut ?: 30000, TimeUnit.MILLISECONDS) // 30 segundos
                .readTimeout(timeOut ?: 30000, TimeUnit.MILLISECONDS)


            val gson = GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setPrettyPrinting()
                .setVersion(1.0)
                .create()

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build()
        }

        private fun networkInterceptor(timeCache: Int = 120): Interceptor {
            return Interceptor { chain ->
                Log.i("cache", "online interceptor chamado")
                //Se houver Internet, pegue o cache que foi armazenado há 5 segundos.
                //Se o cache tiver mais de 2 minutos (120 segundos), descarte-o,
                //e refaz a requisição.
                //O atributo 'max-age' é responsável por este comportamento.
                val response = chain.proceed(chain.request())
                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxAge(timeCache, TimeUnit.SECONDS)
                    .build()
                response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
        }

        private fun offlineInterceptor(): Interceptor {
            return Interceptor { chain ->
                Log.i("cache", "offline interceptor chamado")
                Log.i("cache", "offline interceptor tem rede? - "+ MyApplication.hasNetwork())

                var request = chain.request()

                // prevent caching when network is on. For that we use the "networkInterceptor"
                if (!MyApplication.hasNetwork()) {
                    //Se não houver Internet, pegue o cache que foi armazenado há 7 dias.
                    //Se o cache tiver mais de 7 dias, descarte-o,
                    //e indicam um erro na busca da resposta.
                    //O atributo 'max-stale' é responsável por este comportamento.
                    //O atributo 'only-if-cached' indica não recuperar novos dados; buscar o cache apenas em vez disso.
                    /*request = request.newBuilder().header(
                                "Cache-Control",
                                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                            ).build()*/
                    val cacheControl: CacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()
                    request = request.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .cacheControl(cacheControl)
                        .build()
                    Log.i("cache", "cache control - "+cacheControl)
                }
                chain.proceed(request)
            }
        }
    }
}