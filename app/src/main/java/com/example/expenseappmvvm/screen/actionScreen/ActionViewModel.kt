package com.example.expenseappmvvm.screen.actionScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.ExpenseRepository
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryModel
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
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
    val date = SingleLiveEvent<Long>().apply { value = Calendar.getInstance().timeInMillis }
    var category =MutableLiveData<CategoryModel>().apply { value = CategoryModel(R.drawable.img_income, "Income") }

    private val userId = prefs.getUserId()

    private val currentAmount = MutableLiveData<Double>().apply { value = 0.0 }

    // PublishSubject si EventBus
    val itemSelectedPublishSubject: PublishSubject<CategoryModel> = PublishSubject.create()

    init {
        selectedItem()
    }

    fun saveExpenseOnClick() {
        expense.value!!.expenseDate = date.value!!
        expense.value!!.expenseCategoryName = category.value!!.title
        expense.value!!.expenseCategoryImg = category.value!!.image
        expense.value!!.expenseUserId = userId

        if (photoName.value == null){
            expense.value!!.expensePhoto = ""
        } else {
            expense.value!!.expensePhoto = photoName.value!!
        }

        if (category.value!!.image == R.drawable.img_income) {
            expense.value!!.expenseAmount = amount.value!!.toDouble()
        } else {
            expense.value!!.expenseAmount = -amount.value!!.toDouble()
        }

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

    fun getExpenseDataFromDB(expenseId: Long): Expense {
        expense.value?.let {
            expenseRepository.getExpense(expenseId)
                .subscribeOn(rxSchedulers.background())
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    expense.value = it
                }, {
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }

        return expense.value!!
    }

    fun updateExpenseOnClick() {
        expense.value!!.expenseDate = date.value!!
        expense.value!!.expenseCategoryName = category.value!!.title
        expense.value!!.expenseCategoryImg = category.value!!.image
        expense.value!!.expenseUserId = userId

        if (photoName.value == null){
            expense.value!!.expensePhoto = ""
        } else {
            expense.value!!.expensePhoto = photoName.value!!
        }

        if (category.value!!.image == R.drawable.img_income) {
            expense.value!!.expenseAmount = amount.value!!.toDouble()
        } else {
            expense.value!!.expenseAmount = -amount.value!!.toDouble()
        }

        expense.value?.let { expense ->
            expenseRepository.updateExpense(expense)
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

    fun getCurrentBalance() {
        currentAmount.value?.let {
            expenseRepository.getCurrentBalance(userId)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    //TODO
                    expense.value!!.expenseRemainedAmount = it + expense.value!!.expenseAmount
                }, {
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
                Timber.e(it.localizedMessage)
            })
            .disposeBy(compositeDisposable)
    }

}