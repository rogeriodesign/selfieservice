package br.com.acbr.acbrselfservice.ui.splash

import android.content.ContentResolver

interface SplashContract {
    interface Controller {
        fun saveDeviceUniqueId (resolver: ContentResolver)
    }
    interface UseCase {
        fun saveDeviceUniqueId (resolver: ContentResolver, onPostExecute: (Boolean) -> Unit)
    }
}