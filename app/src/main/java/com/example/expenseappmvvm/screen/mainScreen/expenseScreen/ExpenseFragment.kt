package com.example.expenseappmvvm.screen.mainScreen.expenseScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.FragmentExpenseBinding
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_expense.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExpenseFragment : Fragment() {
    private val expenseViewModel: ExpenseViewModel by viewModel()

    private val buttons = arrayOf(
        R.string.txt_btn_week,
        R.string.txt_btn_month,
        R.string.txt_btn_year
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentExpenseBinding>(inflater, R.layout.fragment_expense, container, false).apply {
            viewModel = this@ExpenseFragment.expenseViewModel
            lifecycleOwner = this@ExpenseFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = vp_expenses
        val tabLayout = tab_expenses_nav

        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = requireContext().resources.getString(buttons[position])
        }.attach()

    }
}