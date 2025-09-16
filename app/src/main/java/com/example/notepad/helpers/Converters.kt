package com.example.notepad.helpers

import androidx.room.TypeConverter
import java.util.Date

public class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) null else date.getTime()
    }
}