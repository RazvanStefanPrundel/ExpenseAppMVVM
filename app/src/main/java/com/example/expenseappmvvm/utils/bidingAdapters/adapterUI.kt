package com.example.expenseappmvvm.utils.bidingAdapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

@BindingAdapter("customText")
fun setCustomText(textView: TextView, customText: Int) {
    textView.text = if (customText != 0)
        textView.context.getString(customText)
    else ""
}