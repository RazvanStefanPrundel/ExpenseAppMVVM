package com.example.expenseappmvvm.screen.mainScreen.budgetScreen

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.FragmentBudgetBinding
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

    fun initComponents(){
        budgetViewModel.initAmounts()
    }

    fun initObservers(){

    }

    companion object {
        fun newInstance() = BudgetFragment()
    }
}