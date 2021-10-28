package com.example.expenseappmvvm.screen.mainScreen.expenseScreen

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.FragmentExpenseBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExpenseFragment : Fragment() {
    private val expenseViewModel: ExpenseViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        DataBindingUtil.setContentView<FragmentExpenseBinding>(
            requireActivity(),
            R.layout.fragment_expense
        ).apply {
            viewModel = this@ExpenseFragment.expenseViewModel
            lifecycleOwner = this@ExpenseFragment
        }

    }

    companion object {
        fun newInstance() = ExpenseFragment()
    }
}