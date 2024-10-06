package br.com.acbr.acbrselfservice.repository.converters

import androidx.room.TypeConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT) //2022-01-16T22:05:36.294000Z

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        if (value != null) {
            try {
                return formatter.parse(value)
            }
            catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }
        else
            return null
    }

    @TypeConverter
    fun dateToTimestamp(value: Date?): String? {
        return if (value != null)
            formatter.format(value)
        else
            null
    }
}