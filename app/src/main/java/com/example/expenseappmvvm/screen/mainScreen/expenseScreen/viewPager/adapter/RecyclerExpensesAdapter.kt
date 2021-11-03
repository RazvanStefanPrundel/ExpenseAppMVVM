package com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.ViewPagerFragment
import com.example.expenseappmvvm.utils.CalendarUtils
import kotlinx.android.synthetic.main.item_expense.view.*
import java.text.SimpleDateFormat

class RecyclerExpensesAdapter(
    private var dataList: List<ExpensesModel>,
    private val expenseItemClickListener: ViewPagerFragment
) :
    RecyclerView.Adapter<RecyclerExpensesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerExpensesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerExpensesAdapter.ViewHolder, position: Int) {
        holder.bind(dataList[position], position)

        holder.itemView.setOnClickListener {
            expenseItemClickListener.expenseClick(dataList[position].expenseId)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ExpensesModel, position: Int) {
            with(itemView) {

                val todayS = CalendarUtils.getStartOfDay(0, 0)
                val todayE = CalendarUtils.getStartOfDay(23, 59)
                val sdf = SimpleDateFormat("E d LLL")

                when (data.date) {
                    in todayS..todayE -> {
                        tv_expense_date.text = "Today" }
                    in (todayS-86400000)..(todayE-86400000) -> {
                        tv_expense_date.text = "Yesterday" }
                    in (todayS+86400000)..(todayE+86400000) -> {
                        tv_expense_date.text = "Tomorrow" }
                    else -> {
                        tv_expense_date.text = sdf.format(data.date).toString() }
                }

                if (data.amount < 0.0) {
                    tv_expense_value.setTextColor(resources.getColor(R.color.red))
                    tv_expense_value.text = data.amount.toString()
                } else {
                    tv_expense_value.setTextColor(resources.getColor(R.color.green))
                    tv_expense_value.text = data.amount.toString()
                }

                if (data.rAmount < 0.0) {
                    tv_expense_value_remain.setTextColor(resources.getColor(R.color.red))
                    tv_expense_value_remain.text = data.rAmount.toString()
                } else {
                    tv_expense_value_remain.setTextColor(resources.getColor(R.color.green))
                    tv_expense_value_remain.text = data.rAmount.toString()
                }

                iv_expense_type.setImageResource(data.categoryImg)
                tv_category_type.text = data.categoryName
                tv_expense_ei.text = data.type

            }
        }
    }
}