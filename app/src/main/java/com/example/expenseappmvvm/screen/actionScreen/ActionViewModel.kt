package com.example.expenseappmvvm.screen.actionScreen

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.ExpenseRepository
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryModel
import com.example.expenseappmvvm.utils.DataStorage
import com.example.expenseappmvvm.utils.DataStorage.remainedAmount
import com.example.expenseappmvvm.utils.FormErrorsEnum
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.Validations
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

    val formErrorsList = ObservableArrayList<FormErrorsEnum>()
    val amountErr = MutableLiveData<Int>().apply { value = 0 }

    val amount = MutableLiveData<String>().apply { value = "0.0" }
    val expense = MutableLiveData<Expense>().apply { value = Expense() }
    val date = SingleLiveEvent<Long>().apply { value = Calendar.getInstance().timeInMillis }

    private var isInvalid = true

    private val userId = prefs.getUserId()

    private var category = MutableLiveData<CategoryModel>().apply { value = CategoryModel(R.drawable.img_income, "Income") }

    // PublishSubject si EventBus
    val itemSelectedPublishSubject: PublishSubject<CategoryModel> = PublishSubject.create()

    init {
        selectedItem()
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

    fun amountTextChanged(amountTextInputEditText: EditText) {
        amountTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                formErrorsList.clear()
                isInvalid = false
                if (validateAmountField()) {
                    FormErrorsEnum.INVALID_AMOUNT.addFormError()
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun validateAmount() {

        formErrorsList.clear()

        isInvalid = false
        if (validateAmountField()) {
            FormErrorsEnum.INVALID_AMOUNT.addFormError()
        }
        if (!isInvalid) {
            if (DataStorage.actionIndex == 0) {
                saveExpenseOnClick()
            } else {
                updateExpenseOnClick()
            }
            DataStorage.actionIndex = 0
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

    private fun assignExpenseData() {

        expense.value!!.expenseDate = date.value!!
        expense.value!!.expenseCategoryName = category.value!!.title
        expense.value!!.expenseCategoryImg = category.value!!.image
        expense.value!!.expenseUserId = userId
        expense.value!!.expensePhoto = photoName.value!!

        if (category.value!!.image == R.drawable.img_income) {
            expense.value!!.expenseAmount = amount.value!!.toDouble()
            expense.value!!.expenseRemainedAmount = remainedAmount + amount.value!!.toDouble()
        } else {
            expense.value!!.expenseAmount = -amount.value!!.toDouble()
            expense.value!!.expenseRemainedAmount = remainedAmount - amount.value!!.toDouble()
        }
    }

    private fun saveExpenseOnClick() {

        assignExpenseData()

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

    private fun updateExpenseOnClick() {

        assignExpenseData()

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

    private fun validateAmountField(): Boolean {
        return when {
            Validations.isAmountInvalid(amount.value!!) -> {
                amountErr.value = R.string.amount_valid_number
                true
            }
            Validations.isAmountEmpty(amount.value!!) -> {
                amountErr.value = R.string.field_empty
                true
            }
            else -> {
                amountErr.value = 0
                false
            }
        }
    }

    private fun FormErrorsEnum.addFormError() {
        formErrorsList.add(this)
        isInvalid = true
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