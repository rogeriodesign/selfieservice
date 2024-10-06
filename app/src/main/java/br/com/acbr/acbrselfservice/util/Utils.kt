package br.com.acbr.acbrselfservice.util

import android.text.TextUtils
import android.util.Patterns
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object{
        private val decimalMonetaryFormat = DecimalFormat("###,###,##0.00")

        fun toMonetaryFormat(value: Double?): String{
            return if(value != null) decimalMonetaryFormat.format(value) else "0,00"
        }

        fun toMonetarySymbolFormat(value: Double?): String{
            return "R$ "+toMonetaryFormat(value)
        }

        fun toDistanceFormat(value: Double?): String{
            return String.format("%.1f",value).replace(".",",")
        }

        fun toQuantityFormat(value: Double?): String{
            if(value != null) {
                if (value % 1.000 == 0.000) {
                    return value.toInt().toString()
                } else {
                    return value.toString()
                }
            } else {
                return "0"
            }
        }

        fun metersToKilometers(value: Double?): Double{
            return ((value?:0).toDouble() / 1000)
        }

        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidPhone(cellphone: String): Boolean {
            var phone = onlyNumbers(cellphone)
            return phone.length == 11
        }

        fun onlyNumbers(numberWithMask: String): String {
            return Regex("[^0-9]").replace(numberWithMask, "")
        }
        val dateBrazilianFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    }
}