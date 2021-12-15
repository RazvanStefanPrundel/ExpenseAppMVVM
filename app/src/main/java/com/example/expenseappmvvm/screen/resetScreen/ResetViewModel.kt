package com.example.expenseappmvvm.screen.resetScreen

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.data.database.entities.User
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.utils.FormErrorsEnum
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.Validations
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ResetViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val userRepository: UserRepository
) : ViewModel() {
    val email = MutableLiveData<String>()
    val checkStatus = SingleLiveEvent<Boolean>()
    val user = MutableLiveData<User>().apply { value = User() }

    val formErrorsList = ObservableArrayList<FormErrorsEnum>()

    private var isValid = true

    fun verifyEmailInDatabase() {
        user.value?.let {
            userRepository.getUserByEmail(user.value!!.userEmail)
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    checkStatus.value = true
                }, {
                    checkStatus.value = false
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    fun emailTextChanged(emailTextInputEditText: TextInputEditText) {
        emailTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                formErrorsList.clear()
                isValid = true
                if (!Validations.isEmailInvalid(user.value!!.userEmail)) {
                    addFormError(FormErrorsEnum.INVALID_EMAIL)
                }
            }
        })
    }

    private fun addFormError(formError: FormErrorsEnum) {
        formErrorsList.add(formError)
        isValid = false
    }

}