package br.com.acbr.acbrselfservice.ui.splash

import android.content.ContentResolver
import android.provider.Settings

class SplashUseCase (): SplashContract.UseCase {


    override fun saveDeviceUniqueId (resolver: ContentResolver, onPostExecute: (Boolean) -> Unit){
        val deviceUniqueId = Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID)
//        oauthRepository.saveDeviceUniqueId(deviceUniqueId){
//            if(it != null){
//                onPostExecute(true)
//            } else {
//                onPostExecute(false)
//            }
//        }
    }
}