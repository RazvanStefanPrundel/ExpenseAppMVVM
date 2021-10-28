package com.example.expenseappmvvm.screen.actionScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.ExpenseRepository
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryModel
import com.example.expenseappmvvm.utils.DateUtil.Companion.sdf
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ActionViewModel(
    private val expenseRepository: ExpenseRepository,
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val prefs: PreferencesProvider
) : ViewModel() {

    val openDateTimePicker = SingleLiveEvent<Any>()

    val checkAdd = SingleLiveEvent<Boolean>()
    val photoName = MutableLiveData<String>()
    val deleteImg = SingleLiveEvent<Boolean>()
    val amount = MutableLiveData<String>().apply { value = "0.0" }
    val expense = MutableLiveData<Expense>().apply { value = Expense() }

    // PublishSubject si EventBus
    val itemSelectedPublishSubject: PublishSubject<CategoryModel> = PublishSubject.create()

    val date = MutableLiveData<String>().apply { value = sdf.format(Calendar.getInstance().timeInMillis) }

    private val category = MutableLiveData<CategoryModel>().apply { value = CategoryModel(R.drawable.img_income, R.string.txt_income_category.toString()) }

    init {
        selectedItem()
    }

    fun saveExpenseOnClick() {

        expense.value!!.expenseAmount = amount.value!!.toDouble()
        expense.value!!.expenseCategoryName = category.value!!.title
        expense.value!!.expenseCategoryImg = category.value!!.image
        expense.value!!.expensePhoto = photoName.value.toString()
        expense.value!!.expenseUserId = prefs.getUserId()!!

        expense.value?.let { expense ->
            expenseRepository.insertExpense(expense)
                .subscribeOn(rxSchedulers.background())
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    checkAdd.value = true
                }, {
                    checkAdd.value = false
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    fun pickImage() {
        photoName.value = "${UUID.randomUUID()}"
    }

    fun deleteImage() {
        deleteImg.call()
    }

    fun onClickOpenDateTimePicker() {
        openDateTimePicker.call()
    }

    private fun selectedItem() {
        itemSelectedPublishSubject
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
                category.value = it
            }, {

            })
            .disposeBy(compositeDisposable)
    }
}