package com.example.expenseappmvvm.utils

import java.util.*

class CalendarUtils {
    companion object{
        fun getStartOfMonth(monthOfYear: Int, dayOfMonth: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            return calendar.timeInMillis
        }

        fun getStartOfWeek(dayOfWeek: Int): Long{
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH))
            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            return calendar.timeInMillis
        }

        fun getStartOfDay(hourOfDay: Int, minuteOfHour: Int): Long{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val date = calendar.get(Calendar.DATE)
            calendar.set(year, month, date, hourOfDay, minuteOfHour)
            return calendar.timeInMillis
        }

        fun getStartOfYear(dayOfYear: Int): Long{
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)
            return calendar.timeInMillis
        }
    }
}