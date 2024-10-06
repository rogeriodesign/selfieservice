package br.com.acbr.acbrselfservice.util

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.DigitsKeyListener

class Mask {

    fun mask(mask: String): TextWatcher {

        return object : TextWatcher {

            var isUpdating: Boolean = false

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //ignored
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //ignored
            }

            override fun afterTextChanged(s: Editable) {
                if (!isUpdating) {
                    isUpdating = true
                    val stringBuilder = StringBuilder()
                    val original = s.toString()
                    var index = 0
                    var j = 0
                    while (index < mask.length) {
                        if (j >= original.length)
                            break
                        if (mask[index] == '#') {
                            stringBuilder.append(original[j])
                            j++
                        } else {
                            if (mask[index] == original[j])
                                j++
                            stringBuilder.append(mask[index])
                        }
                        index++
                    }
                    s.clear()
                    val result = stringBuilder.toString()
                    val filters = s.filters
                    var size = filters.size - 1
                    if (size < 0) {
                        size = 0
                    }
                    val newFilters = arrayOfNulls<InputFilter>(size)
                    var i = 0
                    var thereWasDigitFilter = false
                    for (filter in filters) {
                        if (filter !is DigitsKeyListener) {
                            if (i >= newFilters.size)
                                break
                            newFilters[i] = filter
                            i++
                        } else {
                            thereWasDigitFilter = true
                        }
                    }
                    if (thereWasDigitFilter)
                        s.filters = newFilters
                    s.append(result)
                    isUpdating = false
                }
            }
        }
    }

    companion object {
        fun putTelCelMask(text: String): String{
            var textMask = ""
            var numberIndex = 0
            val number = text.replace("[^0-9]*".toRegex(), "").toCharArray()
            val mask = if (number.size > 10) {
                "(##) #####-####"
            } else {
                "(##) ####-####"
            }

            for (maskItem in mask) {
                if (maskItem != '#') {
                    textMask += maskItem
                } else if(numberIndex < number.size) {
                    textMask += number[numberIndex]
                    numberIndex++
                } else {
                    break
                }
            }
            return textMask
        }
    }
}