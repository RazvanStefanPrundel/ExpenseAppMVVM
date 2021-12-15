package com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.databinding.FragmentViewPagerBinding
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.adapter.ExpensesModel
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.adapter.RecyclerExpensesAdapter
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.dialog.DialogFragment
import com.example.expenseappmvvm.utils.CalendarUtils.Companion.getStartOfMonth
import com.example.expenseappmvvm.utils.CalendarUtils.Companion.getStartOfWeek
import com.example.expenseappmvvm.utils.CalendarUtils.Companion.getStartOfYear
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_view_pager.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

private const val ARG_PARAM = "param"

class ViewPagerFragment : Fragment() {
    private var tabSelected: String? = null
    private val cal = Calendar.getInstance()
    private var dataList = mutableListOf<ExpensesModel>()
    private val viewPagerViewModel: ViewPagerViewModel by viewModel()
    private lateinit var recyclerExpensesAdapter: RecyclerExpensesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentViewPagerBinding>(inflater, R.layout.fragment_view_pager, container, false).apply {
            viewModel = this@ViewPagerFragment.viewPagerViewModel
            lifecycleOwner = this@ViewPagerFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            tabSelected = it.getString(ARG_PARAM)
        }

        initDataInTab(tabSelected)
        initObservers()

    }

    fun expenseClick(expenseId: Long) {
        DialogFragment(requireContext(), expenseId).show(childFragmentManager, DialogFragment.TAG)
    }

    private fun initObservers() {
        viewPagerViewModel.expensesList.observe(viewLifecycleOwner, {
            expensesRCV(it)
        })

        viewPagerViewModel.pieDataSet.observe(viewLifecycleOwner, {
            setPieChart(it)
        })
    }

    private fun initDataInTab(tabSelect: String?) {

        val weekS = getStartOfWeek(cal.firstDayOfWeek)
        val weekE = getStartOfWeek(cal.getActualMaximum(Calendar.DAY_OF_WEEK))

        val monthS = getStartOfMonth(cal.get(Calendar.MONTH), 1)
        val monthE = getStartOfMonth(cal.getActualMaximum(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH))

        val yearS = getStartOfYear(1)
        val yearE = getStartOfYear(cal.getActualMaximum(Calendar.DAY_OF_YEAR))

        when (tabSelect) {
            ViewPagerAdapter.WEEK_TAB -> {
                viewPagerViewModel.initExpensesAmount(weekS, weekE)
            }

            ViewPagerAdapter.MONTH_TAB -> {
                viewPagerViewModel.initExpensesAmount(monthS, monthE)
            }

            ViewPagerAdapter.YEAR_TAB -> {
                viewPagerViewModel.initExpensesAmount(yearS, yearE)
            }
        }

    }

    private fun expensesRCV(expenses: List<Expense>) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        rcv_expenses.layoutManager = linearLayoutManager

        for (expense in expenses) {
            dataList.add(
                ExpensesModel(
                    expense.expenseId,
                    expense.expenseDate,
                    expense.expenseCategoryName,
                    expense.expenseCategoryImg,
                    expense.expenseAmount,
                    expense.expenseCategoryName,
                    expense.expenseRemainedAmount
                )
            )
        }

        recyclerExpensesAdapter = RecyclerExpensesAdapter(dataList, this)
        rcv_expenses.adapter = recyclerExpensesAdapter
    }

    private fun setPieChart(pieDataSet: PieDataSet){

        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        pieDataSet.sliceSpace = 2f
        pieDataSet.valueTextSize = 10f
        pieDataSet.sliceSpace = 5f
        chart_expenses.centerText = "Expenses %"
        chart_expenses.animateY(1500, Easing.EaseInOutQuad)
        chart_expenses.data = PieData(pieDataSet)
        chart_expenses.invalidate()

    }

    companion object {
        @JvmStatic
        fun newInstance(param: String) =
            ViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, param)
                }
            }
    }

}