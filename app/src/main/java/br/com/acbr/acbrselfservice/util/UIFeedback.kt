package br.com.acbr.acbrselfservice.util

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import br.com.acbr.acbrselfservice.R

class UIFeedback {

    companion object {
        var sDialog: AlertDialog? = null
        var sProgressDialog: ProgressDialog? = null

        private fun initDialog(context: Context) {
            if (sDialog == null) {
                sDialog = AlertDialog.Builder(context).create()
            }
        }

        fun showDialog(context: Context, title: String, message: String?, buttonOk: Boolean = false) {
            if(buttonOk){
                showCustomDialog(
                    context = context,
                    title = title,
                    message = message,
                    isCancelable = false,
                    buttonTitlePositive = context.getString(R.string.label_ok),
                    positiveListener = DialogInterface.OnClickListener { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                )
            } else {
                showDialog(context, title, message, 0, true, null)
            }
        }

        fun showDialog(context: Context, message: Int) {
            showDialog(context, null, null, message, true, null)
        }

        fun showDialog(context: Context, message: String?) {
            showDialog(context, null, message, 0, true, null)
        }

        fun showDialog(
            context: Context,
            message: Int,
            isCancelable: Boolean?
        ) {
            showDialog(context, null, null, message, isCancelable, null)
        }

        fun showDialog(
            context: Context,
            message: String?,
            cancelListener: DialogInterface.OnCancelListener?
        ) {
            showDialog(context, null, message, 0, true, cancelListener)
        }

        private fun showDialog(
            context: Context,
            title: String?,
            message: String?,
            resourceMessage: Int,
            isCancelable: Boolean?,
            cancelListener: DialogInterface.OnCancelListener?
        ) {
            initDialog(context)
            title?.let { sDialog?.setTitle(it) }
            sDialog?.setCancelable(isCancelable?:true)
            sDialog?.setMessage(message ?: context.getString(resourceMessage) ?: "")
            sDialog?.setOnCancelListener(cancelListener)
            sDialog?.let {
                if (!it.isShowing) {
                    it.show()
                }
            }
        }

        fun showCustomDialog(
            context: Context,
            title: String?,
            message: String?,
            isCancelable: Boolean = true,
            view: View? = null,
            buttonTitlePositive: String? = null,
            buttonTitleCancel: String? = null,
            positiveListener: DialogInterface.OnClickListener? = null,
            cancelListener: DialogInterface.OnClickListener? = null,
            buttonGreenPositive: Boolean? = false,
            buttonRedNegative: Boolean? = false
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(isCancelable)
            message?.let {
                builder.setMessage(it)
            }

            if (positiveListener != null) {
                builder.setPositiveButton(
                    buttonTitlePositive ?: "OK",
                    positiveListener)
            }

            if (cancelListener != null) {
                builder.setNegativeButton(
                    buttonTitleCancel ?: context.getString(R.string.label_cancel),
                    cancelListener
                )
            }

            sDialog = builder.create()
            if(view!= null) {
                sDialog?.setView(view)
            }
            if(title!= null) {
                sDialog?.setTitle(title)
            }
            sDialog?.setCanceledOnTouchOutside(isCancelable)
            sDialog?.let {
                if (!it.isShowing) {
                    it.show()
                }
            }

            /*if(buttonGreenPositive != null && buttonGreenPositive) {
                val buttonOk = (sDialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE)//AlertDialog.BUTTON_POSITIVE
                buttonOk?.let {
                    it.setTextColor(Color.WHITE)
                    val res = context.resources.getDrawable(R.drawable.btn_green)
                    it.background = res
                }
            }*/

            if(buttonRedNegative != null && buttonRedNegative) {
                val buttonCancel = (sDialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE)//AlertDialog.BUTTON_POSITIVE
                buttonCancel?.let {
                    it.setTextColor(Color.RED)
                }
            }
        }

        /*fun showInputDialog(
            context: Context,
            title: String?,
            message: String?,
            onPostExecute: (String) -> Unit
        ) {
            val inflater: LayoutInflater = context.layoutInflater
            val view = inflater.inflate(R.layout.alert_input_text, null)

            val listener = DialogInterface.OnClickListener { dialogInterface, _ ->
                onPostExecute(if(view.tit_field.text.isNullOrBlank())"" else view.tit_field.text.toString())
                dialogInterface.dismiss()
            }

            showCustomDialog(
                context = context,
                title = title,
                message = message,
                isCancelable = false,
                view = view,
                buttonTitlePositive = context.getString(R.string.btn_confirm),
                buttonTitleCancel = context.getString(R.string.cancel),
                positiveListener = listener,
                cancelListener = DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            )

        }*/


        fun dismissDialog() {
            sDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
        }

        fun releaseVariables() {
            sDialog = null
            sProgressDialog = null
        }

        /*fun getPassword(
            context: Context,
            level: PasswordLevel,
            configCnpj: String? = null,
            onPostIncorrectPassword: () -> Unit,
            onPostCorrectPassword: () -> Unit
        ) {
            getPasswordDefault(
                context,
                null,
                null,
                level,
                configCnpj,
                onPostIncorrectPassword,
                onPostCorrectPassword
            )
        }

        fun getPasswordInputted(
            context: Context,
            inputText: String,
            message: String?,
            configCnpj: String? = null,
            onPostIncorrectPassword: () -> Unit,
            onPostCorrectPassword: () -> Unit
        ) {
            getPasswordDefault(
                context,
                inputText,
                message,
                null,
                configCnpj,
                onPostIncorrectPassword,
                onPostCorrectPassword
            )
        }


        private fun getPasswordDefault(
            context: Context,
            inputText: String?,
            message: String?,
            level: PasswordLevel?,
            configCnpj: String? = null,
            onPostIncorrectPassword: () -> Unit,
            onPostCorrectPassword: () -> Unit
        ) {
            // layout
            val inflater: LayoutInflater = context.layoutInflater
            val view = inflater.inflate(R.layout.alert_input_password, null)
            //view.tit_field!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            var messageText = ""
            var listenerPositive = DialogInterface.OnClickListener{dialogInterface, _ -> dialogInterface.dismiss()}

            if (message.isNullOrBlank()){
                level?.let {
                    messageText = when(it) {
                        PasswordLevel.Attendant -> context.getString(R.string.msg_passwdateatu)
                        PasswordLevel.Supervisor -> context.getString(R.string.msg_passwdsup)
                        PasswordLevel.Administrator -> context.getString (R.string.msg_passwdadm)
                        PasswordLevel.Support -> context.getString(R.string.msg_confexit)
                    }
                }
            } else {
                messageText = message
            }

            listenerPositive = DialogInterface.OnClickListener { dialogInterface, _ ->
                if (level != null && validatePassword(context, view.tit_field.text.toString(), level, null, configCnpj) ||
                    level == null && validatePassword(context, view.tit_field.text.toString(), null, inputText, configCnpj)) {
                    onPostCorrectPassword()
                } else {
                    onPostIncorrectPassword()
                    showDialog(
                        context,
                        context.getString(R.string.app_name),
                        context.getString(R.string.msg_nopasswdold),
                        true
                    )
                }
                dialogInterface.dismiss()
            }

            showCustomDialog(
                context = context,
                title = context.getString(R.string.app_name),
                message = messageText,
                isCancelable = false,
                view = view,
                buttonTitlePositive = context.getString(R.string.btn_confirm),
                buttonTitleCancel = context.getString(R.string.cancel),
                positiveListener = listenerPositive,
                cancelListener = DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                },
                buttonGreenPositive = true
            )
        }

        private fun getPasswordSupport(configCnpj: String?, context: Context): String {
            return Util.getPasswordSupportCurrent(
                context.applicationContext,
                userCurrentGlobal.email!!,
                if (configCnpj.isNullOrBlank()) {
                    if (userCurrentGlobal.cnpj.isNullOrBlank()) {
                        Constants.cnpjgvr
                    } else
                        userCurrentGlobal.cnpj ?: ""
                } else
                    configCnpj
            )
        }

        private fun getPasswordAdmin(
            context: Context,
            repository: repositoyDB
        ): String {
            var password = ""
            val retpass = repository.loadpasswd(context.applicationContext, 0, 1)
            if (retpass.outreturn)
                password = retpass.outpasswd[0].passwordNew!!
            return password
        }

        private fun getPasswordManager(
            context: Context,
            repository: repositoyDB
        ): String {
            var password = ""
            val retpass = repository.loadpasswd(context.applicationContext, 0, 2)
            if (retpass.outreturn)
                password = retpass.outpasswd[0].passwordNew!!
            return password
        }

        private fun getPasswordEmployee(
            context: Context,
            repository: repositoyDB
        ): String {
            // pega a senha do atendente
            var password = ""
            val retate = repository.loadEmployee(
                context.applicationContext,
                userCurrentGlobal.cnpj ?: "",
                shiftCurrentGlobal.outturno.employeeid
            )
            if (retate.outreturn) {
                password =
                    if (retate.outemployee[0].password.isNullOrBlank()) "" else retate.outemployee[0].password!!
            }
            return password
        }

        private fun validatePassword(context: Context, original: String, level: PasswordLevel?, alternative: String? = null, configCnpj: String? = null): Boolean{
            val repository = repositoyDB()
            val passwordSupport = getPasswordSupport(configCnpj, context)
            if(level == null){
                if (Util.toMD5Hash(original) == alternative || original == passwordSupport) {
                    return true
                }
            } else {
                when (level) {
                    PasswordLevel.Attendant -> {
                        val passwordEmployee = getPasswordEmployee(context, repository)
                        if (Util.toMD5Hash(original) == passwordEmployee || original == passwordSupport) {
                            return true
                        }
                    }
                    PasswordLevel.Supervisor -> {
                        val passwordManager = getPasswordManager(context, repository)
                        val passwordAdmin = getPasswordAdmin(context, repository)
                        if (Util.toMD5Hash(original) == passwordManager || Util.toMD5Hash(original) == passwordAdmin || original == passwordSupport) {
                            return true
                        }
                    }
                    PasswordLevel.Administrator -> {
                        val passwordAdmin = getPasswordAdmin(context, repository)
                        if (Util.toMD5Hash(original) == passwordAdmin || original == passwordSupport) {
                            return true
                        }
                    }
                    PasswordLevel.Support -> {
                        if (original == passwordSupport) {
                            return true
                        }
                    }
                }
            }
            return false
        }*/
    }
}