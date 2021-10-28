package com.example.expenseappmvvm.screen.actionScreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseappmvvm.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_layout_category.view.*

class CategoryAdapter(
    private var dataList: List<CategoryModel>,
    private var onClickInterface: PublishSubject<CategoryModel>
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var positionSelected = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: CategoryModel, position: Int) {
            with(itemView) {
                tv_category_name.text = data.title
                img_category.setImageResource(data.image)

                category_layout.setOnClickListener {

                    positionSelected = position
                    notifyDataSetChanged()

                    onClickInterface.onNext(data)
                }

                if (positionSelected == position) {
                    container.setBackgroundResource(R.drawable.bg_expense_type_rectangle_selected)
                } else {
                    container.setBackgroundResource(R.drawable.bg_expense_type_rectangle)
                }
            }
        }
    }

}