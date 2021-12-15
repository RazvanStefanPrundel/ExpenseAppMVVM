package com.example.expenseappmvvm.screen.registerScreen

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

class RegisterViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val rxSchedulers: AppRxSchedulers,
    private val userRepository: UserRepository,
    private val prefs: PreferencesProvider
) : ViewModel() {

    val redirectToLogin = SingleLiveEvent<Any>()
    val registerStatus = SingleLiveEvent<Boolean>()

    val user = MutableLiveData<User>().apply { value = User() }

    val formErrorsList = ObservableArrayList<FormErrorsEnum>()
    val passwordErr = MutableLiveData<Int>().apply { value = 0 }

    private var isInvalid = true

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun nameTextChanged(registerTextInputEditText: TextInputEditText) {
        registerTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                formErrorsList.clear()
                isInvalid = false
                if (Validations.isNameInvalid(user.value!!.userName)) {
                    addFormError(FormErrorsEnum.MISSING_NAME)
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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

    fun validateRegister() {
        formErrorsList.clear()
        isInvalid = false
        if (Validations.isNameInvalid(user.value!!.userName)) {
            addFormError(FormErrorsEnum.MISSING_NAME)
        }
        if (Validations.isEmailInvalid(user.value!!.userEmail)) {
            addFormError(FormErrorsEnum.INVALID_EMAIL)
        }
        if (validatePassword()) {
            addFormError(FormErrorsEnum.INVALID_PASSWORD)
        }
        if (!isInvalid) {
            registerIntoDatabase()
        }
    }

    fun onClickLogin() {
        redirectToLogin.call()
    }

    private fun registerIntoDatabase() {
        user.value?.let { user ->
            userRepository.insertUser(user)
                .subscribeOn(rxSchedulers.background())
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    registerStatus.value = true
                    redirectToLogin.call()
                }, {
                    registerStatus.value = false
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    private fun addFormError(formError: FormErrorsEnum) {
        formErrorsList.add(formError)
        isInvalid = true
    }

    private fun validatePassword(): Boolean {
        when {
            Validations.passwordEmpty(user.value!!.userPassword) -> {
                passwordErr.value = R.string.password_err_empty
                return true
            }
            Validations.passwordContainsDigits(user.value!!.userPassword) -> {
                passwordErr.value = R.string.password_err_digits
                return true
            }
            Validations.passwordContainsLowercase(user.value!!.userPassword) -> {
                passwordErr.value = R.string.password_err_lowercase
                return true
            }
            Validations.passwordContainsUppercase(user.value!!.userPassword) -> {
                passwordErr.value = R.string.password_err_uppercase
                return true
            }
            Validations.passwordContainsSpecialChar(user.value!!.userPassword) -> {
                passwordErr.value = R.string.password_err_specialChar
                return true
            }
            Validations.passwordExcludesSpace(user.value!!.userPassword) -> {
                passwordErr.value = R.string.password_err_whiteSpace
                return true
            }
            Validations.passwordLength(user.value!!.userPassword) -> {
                passwordErr.value = R.string.password_err_length
                return true
            }
            else -> {
                passwordErr.value = 0
                return false
            }
        }
    }
}