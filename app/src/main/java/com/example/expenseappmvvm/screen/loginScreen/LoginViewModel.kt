package com.example.expenseappmvvm.screen.loginScreen

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.User
import com.example.expenseappmvvm.data.database.prefs.PreferencesProvider
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import com.example.expenseappmvvm.utils.FormErrorsEnum
import com.example.expenseappmvvm.utils.SingleLiveEvent
import com.example.expenseappmvvm.utils.Validations
import com.example.expenseappmvvm.utils.rxUtils.AppRxSchedulers
import com.example.expenseappmvvm.utils.rxUtils.disposeBy
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class LoginViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val userRepository: UserRepository,
    private val prefs: PreferencesProvider
) : ViewModel() {
    val redirectToMain = SingleLiveEvent<Any>()
    val redirectToReset = SingleLiveEvent<Any>()
    val redirectToRegister = SingleLiveEvent<Any>()

    val loginStatus = SingleLiveEvent<Boolean>()
    val user = MutableLiveData<User>().apply { value = User() }

    val formErrorsList = ObservableArrayList<FormErrorsEnum>()
    val passwordErr = MutableLiveData<Int>().apply { value = 0 }

    private var isInvalid = true

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun emailTextChanged(emailTextInputEditText: TextInputEditText) {
        emailTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                formErrorsList.clear()
                isInvalid = false
                if (Validations.isEmailInvalid(user.value!!.userEmail)) {
                    addFormError(FormErrorsEnum.INVALID_EMAIL)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun passwordTextChanged(passwordTextInputText: TextInputEditText) {
        passwordTextInputText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                formErrorsList.clear()
                isInvalid = false
                if (validatePassword()) {
                    addFormError(FormErrorsEnum.INVALID_PASSWORD)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun validateLogin() {
        formErrorsList.clear()
        isInvalid = false
        if (Validations.isEmailInvalid(user.value!!.userEmail)) {
            addFormError(FormErrorsEnum.INVALID_EMAIL)
        }
        if (validatePassword()) {
            addFormError(FormErrorsEnum.INVALID_PASSWORD)
        }
        if (!isInvalid) {
            verifyUserLoginData()
        }
    }

    fun onClickRegister() {
        redirectToRegister.call()
    }

    fun onClickReset() {
        redirectToReset.call()
    }

    private fun verifyUserLoginData() {
        user.value?.let {
            userRepository.getUserLogin(user.value!!.userEmail, user.value!!.userPassword)
                .subscribeOn(rxSchedulers.background())
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    loginStatus.value = true
                    prefs.saveUserId(it.userId)
                    redirectToMain.call()
                }, {
                    loginStatus.value = false
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    private fun validatePassword(): Boolean {
        return when {
            Validations.passwordEmpty(user.value!!.userPassword) -> {
                passwordErr.value = R.string.password_err_empty
                true
            }
            else -> {
                passwordErr.value = 0
                false
            }
        }
    }

    private fun addFormError(formError: FormErrorsEnum) {
        formErrorsList.add(formError)
        isInvalid = true
    }

}