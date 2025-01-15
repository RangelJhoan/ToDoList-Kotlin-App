package com.rangeljhoandev.todolist_kotlin_app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {

    fun formatDate2String(date: Date, pattern: String): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(date)

}