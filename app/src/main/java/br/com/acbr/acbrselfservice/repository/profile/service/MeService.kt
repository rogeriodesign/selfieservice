package br.com.acbr.acbrselfservice.repository.profile.service

import br.com.acbr.acbrselfservice.repository.GenericCallback
import br.com.acbr.acbrselfservice.repository.RestGenericService
import br.com.acbr.acbrselfservice.util.Resource
import okhttp3.ResponseBody

class MeService {

    fun getProfileData(
        authorization: String,
        onPostExecute: (Resource<ProfileResponseDto>) -> Unit
    ){
        val service = RestGenericService().getMeService()
        service.getProfileData(authorization).enqueue(
            GenericCallback<ProfileResponseDto>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun nameUpdate(
        request: NameRequestDto,
        authorization: String,
        onPostExecute: (Resource<ProfileResponseDto>) -> Unit
    ){
        val service = RestGenericService().getMeService()
        service.nameUpdate(request, authorization).enqueue(
            GenericCallback<ProfileResponseDto>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun emailUpdate(
        request: EmailRequestDto,
        authorization: String,
        onPostExecute: (Resource<ResponseBody>) -> Unit
    ){
        val service = RestGenericService().getMeService()
        service.emailUpdate(request, authorization).enqueue(
            GenericCallback<ResponseBody>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun validateEmailUpdate(
        request: EmailCodeRequestDto,
        authorization: String,
        onPostExecute: (Resource<ProfileResponseDto>) -> Unit
    ){
        val service = RestGenericService().getMeService()
        service.validateEmailUpdate(request, authorization).enqueue(
            GenericCallback<ProfileResponseDto>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun phoneNumberUpdate(
        request: PhoneRequestDto,
        authorization: String,
        onPostExecute: (Resource<ResponseBody>) -> Unit
    ){
        val service = RestGenericService().getMeService()
        service.phoneNumberUpdate(request, authorization).enqueue(
            GenericCallback<ResponseBody>(
                onPostExecute = onPostExecute
            )
        )
    }

    fun validatePhoneNumberUpdate(
        request: PhoneCodeRequestDto,
        authorization: String,
        onPostExecute: (Resource<ProfileResponseDto>) -> Unit
    ){
        val service = RestGenericService().getMeService()
        service.validatePhoneNumberUpdate(request, authorization).enqueue(
            GenericCallback<ProfileResponseDto>(
                onPostExecute = onPostExecute
            )
        )
    }
}