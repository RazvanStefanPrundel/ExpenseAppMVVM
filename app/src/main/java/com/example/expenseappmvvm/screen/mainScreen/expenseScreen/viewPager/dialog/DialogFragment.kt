package com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.databinding.DialogExpenseInfoBinding
import com.example.expenseappmvvm.screen.actionScreen.ActionActivity
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.ViewPagerFragment
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager.ViewPagerViewModel
import com.example.expenseappmvvm.utils.DataUtils
import com.example.expenseappmvvm.utils.DataUtils.Companion.sdf
import com.example.expenseappmvvm.utils.ImageStorageManager
import kotlinx.android.synthetic.main.dialog_expense_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogFragment(
    private var ctx: Context,
    private var expenseId: Long
) : DialogFragment() {
    private val dialogViewModel: DialogViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return DataBindingUtil.inflate<DialogExpenseInfoBinding>(inflater, R.layout.dialog_expense_info, container, false).apply {
            viewModel = this@DialogFragment.dialogViewModel
            lifecycleOwner = this@DialogFragment
        }.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogViewModel.getExpenseForDialog(expenseId)
        initObserver()
    }

    private fun initObserver() {
        dialogViewModel.expenseForDialog.observe(viewLifecycleOwner, {
            populateDialog(it)
        })

        dialogViewModel.deleteExpense.observe(viewLifecycleOwner, {
            ImageStorageManager.deleteImageFromInternalStorage(ctx, it)
            dismiss()
        })

        dialogViewModel.redirectToEdit.observe(viewLifecycleOwner, {
            val intent = Intent(ctx, ActionActivity::class.java)
            DataUtils.EXPENSE_ID = expenseId
            DataUtils.actionIndex = 1
            ctx.startActivity(intent)
            dismiss()
        })
    }

    private fun populateDialog(expenseInDialog: Expense) {
        dialog_img_category.setImageResource(expenseInDialog.expenseCategoryImg)
        dialog_tv_category_name.text = expenseInDialog.expenseCategoryName
        dialog_et_date.setText(sdf.format(expenseInDialog.expenseDate))
        dialog_et_amount.setText(expenseInDialog.expenseAmount.toString())
        dialog_et_details.setText(expenseInDialog.expenseDetails)

//        val image = ImageStorageManager.getImageFromInternalStorage(ctx, expenseInDialog.expensePhoto)
//        dialog_img_details.setImageBitmap(image)
    }

    companion object {
        const val TAG = "ExpenseDialog"
    }
}