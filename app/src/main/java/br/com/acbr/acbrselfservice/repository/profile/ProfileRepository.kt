package br.com.acbr.acbrselfservice.repository.profile

import android.content.Context
import androidx.lifecycle.LiveData
import br.com.acbr.acbrselfservice.entity.ConfigurationData
import br.com.acbr.acbrselfservice.repository.AppDatabase
import br.com.acbr.acbrselfservice.repository.profile.service.*
import br.com.acbr.acbrselfservice.repository.profile.database.ProfileEntity
import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource
import br.com.acbr.acbrselfservice.util.ResourceStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProfileRepository (context: Context,
                         private val service: MeService) {

    private val dao = AppDatabase.getInstance(context).profileDAO
    private val scope = CoroutineScope(Dispatchers.Main)


    fun save(
        name: String,
        email: String?,
        phone: String?,
        onPostExecute: (ProcessStatus) -> Unit
    ) {
        saveAsync(ProfileEntity(
            id = 1,
            name = name,
            email = email,
            phoneNumber = phone,
            updatedAt = Date()
        )){
            if(it != null){
                onPostExecute(ProcessStatus.Success)
            } else {
                onPostExecute(ProcessStatus.SaveFail)
            }
        }
    }

    fun saveName(
        name: String,
        onPostExecute: (ProcessStatus) -> Unit
    ) {
        find(1){
            if(it != null){
                save(name, it.email, it.phoneNumber, onPostExecute)
            } else {
                onPostExecute(ProcessStatus.SaveFail)
            }
        }
    }

    fun saveEmail(
        email: String,
        onPostExecute: (ProcessStatus) -> Unit
    ) {
        find(1){
            if(it != null){
                save(it.name, email, it.phoneNumber, onPostExecute)
            } else {
                onPostExecute(ProcessStatus.SaveFail)
            }
        }
    }

    fun savePhone(
        phone: String,
        onPostExecute: (ProcessStatus) -> Unit
    ) {
        find(1){
            if(it != null){
                save(it.name, it.email, phone, onPostExecute)
            } else {
                onPostExecute(ProcessStatus.SaveFail)
            }
        }
    }

    fun updateProfile(
        onPostExecute: (ResourceStatus) -> Unit
    ) {
        getProfileInService{
            if(it.status == ProcessStatus.Success && it.data != null){
                saveAsync(
                    ProfileEntity(
                        id = 1,
                        upId = it.data.id,
                        name = it.data.name,
                        email = it.data.email,
                        phoneNumber = it.data.phoneNumber,
                        loginId = it.data.loginId,
                        updatedAt = it.data.updatedAt,
                        createdAt = it.data.createdAt
                    )
                ) { id ->
                    if(id != null){
                        onPostExecute(ResourceStatus("", ProcessStatus.Success))
                    } else {
                        onPostExecute(ResourceStatus("", ProcessStatus.SaveFail))
                    }
                }
            } else {
                onPostExecute(ResourceStatus(it.message, it.status))
            }
        }
    }

    private fun getProfileInService(
        onPostExecute: (Resource<ProfileResponseDto>) -> Unit
    ){
                service.getProfileData(
                    ""
                ) {
                    if(it.status == ProcessStatus.Success && it.data != null) {
                        onPostExecute(Resource(it.data, "", ProcessStatus.Success))
                    } else if(it.message.indexOf("401", 0, true) > -1) {// token expirado
                                getProfileInService(onPostExecute)
                    } else {
                        onPostExecute(Resource(it.data, it.message, it.status))
                    }
                }
    }



    fun emailUpdate(
        email: String,
        onPostExecute: (Resource<String>) -> Unit
    ){
                service.emailUpdate(
                    EmailRequestDto(
                        email = email,
                        deviceUniqueId = "resourceAuthorization.data.deviceUniqueId"
                    ),
                    ""
                ){
                    val data = if (it.data != null) {
                        it.data.toString()
                    } else {
                        null
                    }

                    if(it.status == ProcessStatus.Success && it.data != null) {
                        onPostExecute(Resource(data, "", ProcessStatus.Success))
                    } else if(it.message.indexOf("401", 0, true) > -1) {// token expirado
                                emailUpdate(email, onPostExecute)
                    } else {
                        onPostExecute(Resource(data, it.message, it.status))
                    }
                }
    }



    fun phoneNumberUpdate(
        phoneNumber: String,
        onPostExecute: (Resource<String>) -> Unit
    ){
                service.phoneNumberUpdate(
                    PhoneRequestDto(
                        phoneNumber = phoneNumber,
                        deviceUniqueId = "resourceAuthorization.data.deviceUniqueId"
                    ),
                    ""
                ){
                    val data = if (it.data != null) {
                        it.data.toString()
                    } else {
                        null
                    }

                    if(it.status == ProcessStatus.Success && it.data != null) {
                        onPostExecute(Resource(data, "", ProcessStatus.Success))
                    } else if(it.message.indexOf("401", 0, true) > -1) {// token expirado
                                phoneNumberUpdate(phoneNumber, onPostExecute)
                    } else {
                        onPostExecute(Resource(data, it.message, it.status))
                    }
                }
    }




    private fun saveAsync(
        profile: ProfileEntity,
        onPostExecute: (Long?) -> Unit
    ) {
        scope.launch {
            val id = saveSync(profile)
            onPostExecute(id)
        }
    }

    private fun remove(
        profile: ProfileEntity,
        onPostExecute: (Int?) -> Unit
    ) {
        scope.launch {
            val lines = removeSync(profile)
            onPostExecute(lines)
        }
    }

    private fun getAll(
        onPostExecute: (List<ProfileEntity>) -> Unit
    ) {
        scope.launch {
            val item = getAllSync()
            onPostExecute(item)
        }
    }

    private fun find(
        id: Long,
        onPostExecute: (ProfileEntity?) -> Unit
    ) {
        scope.launch {
            val item = findSync(id)
            onPostExecute(item)
        }
    }

    private suspend fun saveSync (
        profile: ProfileEntity
    ) = withContext(Dispatchers.IO) {
        val lines = dao.save(profile)
        lines
    }

    private suspend fun removeSync (
        profile: ProfileEntity
    ) = withContext(Dispatchers.IO) {
        val lines = dao.remove(profile)
        lines
    }

    private suspend fun getAllSync () = withContext(Dispatchers.IO) {
        val list = dao.getAll()
        list
    }

    private suspend fun findSync (
        id: Long
    ) = withContext(Dispatchers.IO) {
        val item = dao.findForId(id)
        item
    }


}