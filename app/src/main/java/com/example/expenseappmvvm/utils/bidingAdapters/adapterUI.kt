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

@BindingAdapter("pieChartData")
fun setPieData(pieChart: PieChart ,pieChartData: PieDataSet){

    pieChartData.setColors(*ColorTemplate.COLORFUL_COLORS)
    pieChartData.sliceSpace = 2f
    pieChartData.valueTextSize = 10f
    pieChartData.sliceSpace = 5f
    pieChart.centerText = "Expenses %"
    pieChart.animateY(1500, Easing.EaseInOutQuad)
    pieChart.data = PieData(pieChartData)
    pieChart.invalidate()

}

@BindingAdapter("barChartData")
fun setBarData(barChart: BarChart, barData: BarData){
    barChart.animateY(1500)

    val xAxisLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
    barChart.xAxis.position = XAxis.XAxisPosition.BOTH_SIDED

    barChart.data = barData
    barChart.invalidate()
}