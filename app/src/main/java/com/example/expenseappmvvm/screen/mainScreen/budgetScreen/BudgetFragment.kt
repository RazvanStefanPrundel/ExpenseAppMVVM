package com.example.expenseappmvvm.screen.mainScreen.budgetScreen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.FragmentBudgetBinding
import com.example.expenseappmvvm.utils.CalendarUtils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_budget.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BudgetFragment : Fragment() {
    private val budgetViewModel: BudgetViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return DataBindingUtil.inflate<FragmentBudgetBinding>(inflater, R.layout.fragment_budget, container, false).apply {
            viewModel = this@BudgetFragment.budgetViewModel
            lifecycleOwner = this@BudgetFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        setBarChart()
    }

    private fun initObservers() {
        budgetViewModel.initAmounts()

        budgetViewModel.currentBalance.observe(viewLifecycleOwner, {
            if (it < 0.0) {
                negativeBalance()
            } else {
                positiveBalance()
            }
        })

    }

    private fun positiveBalance() {
        ll_current_balance.setBackgroundResource(R.drawable.border_current_balance_positive)
        tv_current_balance_txt.setTextColor(resources.getColor(R.color.green))
        tv_current_balance.setTextColor(resources.getColor(R.color.green))
    }

    private fun negativeBalance() {
        ll_current_balance.setBackgroundResource(R.drawable.border_current_balance_negative)
        tv_current_balance_txt.setTextColor(resources.getColor(R.color.red))
        tv_current_balance.setTextColor(resources.getColor(R.color.red))
    }

    private fun setBarChart(){
        val sumOfAllMonth = MutableList(12) { 0.0 }

        val barsEntriesP: ArrayList<BarEntry> = ArrayList()
        val barsEntriesN: ArrayList<BarEntry> = ArrayList()
        var barDataSetP = BarDataSet(barsEntriesP, "")
        var barDataSetN = BarDataSet(barsEntriesN, "")

            for (i in 0..11) {
                val firstDayOfMonth = CalendarUtils.getStartOfMonth(i, 1)
                val firstDayOfNextMonth = CalendarUtils.getStartOfMonth(i + 1, 1)

                sumOfAllMonth[i] = budgetViewModel.initBarChartData(firstDayOfMonth, firstDayOfNextMonth)

                if (sumOfAllMonth[i] < 0.0) {
                    barsEntriesN.add(BarEntry(i.toFloat(), sumOfAllMonth[i].toFloat()))
                    barDataSetN = BarDataSet(barsEntriesN, "Negative")
                } else {
                    barsEntriesP.add(BarEntry(i.toFloat(), sumOfAllMonth[i].toFloat()))
                    barDataSetP = BarDataSet(barsEntriesP, "Positive")
                }
            }

        barDataSetN.setColors(Color.RED)
        barDataSetP.setColors(Color.GREEN)
        val barData = BarData()
        barData.addDataSet(barDataSetP)
        barData.addDataSet(barDataSetN)
        chart_budget_details.animateY(1500)

        val xAxisLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        chart_budget_details.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        chart_budget_details.xAxis.position = XAxis.XAxisPosition.BOTH_SIDED

        chart_budget_details.data = barData
        chart_budget_details.invalidate()
    }

}