package br.com.acbr.acbrselfservice.repository

import br.com.acbr.acbrselfservice.util.ProcessStatus
import br.com.acbr.acbrselfservice.util.Resource
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenericCallback<T>(
    private val failMessage: String = "Fail",
    val onPostExecute: (Resource<T>) -> Unit
) : Callback<T> {
    override fun onResponse(
        call: Call<T?>,
        response: Response<T?>
    ) {
        if (response.code() == 200  || response.code() == 201  || response.code() == 204) {
            //if (response.body() != null) {
            onPostExecute(
                Resource(
                    data = response.body(),
                    message = "",
                    status = ProcessStatus.Success
                )
            )
            /*} else {
                onPostExecute(
                    Resource(
                        data = null,
                        message = "Body empty",
                        status = ProcessStatus.Fail
                    )
                )
            }*/
        } else {
            var messageErrorServer = "${response.code()}"

            response.errorBody()?.let {
                messageErrorServer += " - ${parseError(it)}"
            }

            onPostExecute(
                Resource(
                    data = null,
                    message = messageErrorServer,
                    status = ProcessStatus.Fail
                )
            )
        }
    }

    override fun onFailure(call: Call<T?>, t: Throwable) {
        val msg = if (t.message == null)
            failMessage
        else
            t.message!!

        onPostExecute(
            Resource(
                data = null,
                message = msg,
                status = ProcessStatus.Fail
            )
        )
    }

    private fun parseError(response: ResponseBody): String {
        try {
            var messageMaster: String
            val source = response.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer
            val bs = buffer.clone().readString(Charsets.UTF_8)

            /*val json = JsonParser().parse(bs)
            val messageResponse = json
                .asJsonObject["response_message"]
            val messageCode = json
                .asJsonObject["response_code"]
            val messageError = json
                .asJsonObject["error"]
            val messageDetail = json
                .asJsonObject["errors"]
            val messageMsg = json
                .asJsonObject["msg"]
            val message = json
                .asJsonObject["message"]

            if (messageResponse != null) {
                messageMaster = messageResponse.asString
            } else if (messageError != null) {
                messageMaster = messageError.asString
            } else if(messageMsg != null){
                messageMaster = messageMsg.asString
            } else if(message != null){
                messageMaster = message.asString
            }

            if (messageCode != null) {
                messageMaster = messageCode.asString + " - " + messageMaster
            }
            if (messageDetail != null && messageDetail.toString() != "{}") {
                messageMaster += ":  $messageDetail"
            }
            return messageMaster*/

            messageMaster = bs
            return messageMaster

        }
        catch (e: Exception) {
            return e.message.toString()
        }
    }
}