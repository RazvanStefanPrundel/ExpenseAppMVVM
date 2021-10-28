package com.example.expenseappmvvm.screen.mainScreen.budgetScreen

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.FragmentBudgetBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_budget.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BudgetFragment : Fragment() {
    private val budgetViewModel: BudgetViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        DataBindingUtil.setContentView<FragmentBudgetBinding>(
            requireActivity(),
            R.layout.fragment_budget
        ).apply {
            viewModel = this@BudgetFragment.budgetViewModel
            lifecycleOwner = this@BudgetFragment
        }

        initComponents()
        initObservers()
    }

    private fun initComponents(){
        budgetViewModel.initAmounts()
        setBarChart()

        if (budgetViewModel.currentBalance.value!! < 0.0) {
            ll_current_balance.setBackgroundResource(R.drawable.border_current_balance_negative)
            tv_current_balance_txt.setTextColor(resources.getColor(R.color.red))
            tv_current_balance.setTextColor(resources.getColor(R.color.red))
        } else {
            ll_current_balance.setBackgroundResource(R.drawable.border_current_balance_positive)
            tv_current_balance_txt.setTextColor(resources.getColor(R.color.green))
            tv_current_balance.setTextColor(resources.getColor(R.color.green))
        }
    }

    private fun initObservers(){

    }

    private fun setBarChart() {

        budgetViewModel.initBarChartData()

        val barDataSetP = BarDataSet(budgetViewModel.barsEntriesP, "")
        val barDataSetN = BarDataSet(budgetViewModel.barsEntriesN, "")

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

    companion object {
        fun newInstance() = BudgetFragment()
    }
}