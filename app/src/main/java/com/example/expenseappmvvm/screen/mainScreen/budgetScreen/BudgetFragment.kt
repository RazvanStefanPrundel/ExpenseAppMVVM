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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
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
        initComponents()
    }

    private fun initComponents() {
        budgetViewModel.initAmounts()
        budgetViewModel.initBarChartData()
    }

    private fun initObservers() {
        budgetViewModel.currentBalance.observe(viewLifecycleOwner, {
            if (it < 0.0) {
                negativeBalance()
            } else {
                positiveBalance()
            }
        })

        budgetViewModel.barDataSetP.observe(viewLifecycleOwner, {
            it.setColors(Color.GREEN)
        })

        budgetViewModel.barDataSetN.observe(viewLifecycleOwner, {
            it.setColors(Color.RED)
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

}