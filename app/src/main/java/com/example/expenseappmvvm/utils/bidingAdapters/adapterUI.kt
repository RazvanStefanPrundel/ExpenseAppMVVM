package com.example.expenseappmvvm.utils.bidingAdapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("customText")
fun setCustomText(textView: TextView, customText: Int) {
    textView.text = if (customText != 0)
        textView.context.getString(customText)
    else ""
}