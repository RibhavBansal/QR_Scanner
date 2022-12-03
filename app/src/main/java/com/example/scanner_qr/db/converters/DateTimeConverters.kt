package com.example.scanner_qr.db.converters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Calendar

class DateTimeConverters {
    @TypeConverter
    fun toCalendar(l : Long):Calendar?{
        val c = Calendar.getInstance()
        c!!.timeInMillis = l
        return c;
    }

    @TypeConverter
    fun fromCalendar(c:Calendar?):Long?{
        return c?.time?.time
    }
}