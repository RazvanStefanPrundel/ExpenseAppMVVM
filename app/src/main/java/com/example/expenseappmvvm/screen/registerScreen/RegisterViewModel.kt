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
import com.example.expenseappmvvm.screen.splashScreen.SplashViewModel
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
    val autologin = SingleLiveEvent<Any>()
    val redirectToLogin = SingleLiveEvent<Any>()
    val registerStatus = SingleLiveEvent<Boolean>()

    val user = MutableLiveData<User>().apply { value = User() }

    val formErrorsList = ObservableArrayList<FormErrorsEnum>()
    val passwordErr = MutableLiveData<Int>().apply { value = 0 }

    private var isValid = true

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun nameTextChanged(registerTextInputEditText: TextInputEditText) {
        registerTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                formErrorsList.clear()
                isValid = true
                if (!Validations.nameValidation(user.value!!.userName)) {
                    addFormError(FormErrorsEnum.MISSING_NAME)
                }
            }
        })
    }

    fun emailTextChanged(emailTextInputEditText: TextInputEditText) {
        emailTextInputEditText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                formErrorsList.clear()
                isValid = true
                if (!Validations.emailValidation(user.value!!.userEmail)) {
                    addFormError(FormErrorsEnum.INVALID_EMAIL)
                }
            }
        })
    }

    fun passwordTextChanged(passwordTextInputText: TextInputEditText) {
        passwordTextInputText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                formErrorsList.clear()
                isValid = true
                if (!validatePassword()) {
                    addFormError(FormErrorsEnum.INVALID_PASSWORD)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun validateRegister() {
        formErrorsList.clear()
        isValid = true
        if (!Validations.nameValidation(user.value!!.userName)) {
            addFormError(FormErrorsEnum.MISSING_NAME)
        }
        if (!Validations.emailValidation(user.value!!.userEmail)) {
            addFormError(FormErrorsEnum.INVALID_EMAIL)
        }
        if (!validatePassword()) {
            addFormError(FormErrorsEnum.INVALID_PASSWORD)
        }
        if (isValid) {
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
                    prefs.saveUserId(user.userId)
                    autologin.call()
                }, {
                    registerStatus.value = false
                    Timber.e(it.localizedMessage)
                })
                .disposeBy(compositeDisposable)
        }
    }

    private fun addFormError(formError: FormErrorsEnum) {
        formErrorsList.add(formError)
        isValid = false
    }

    private fun validatePassword(): Boolean {
        if (Validations.passwordEmpty(user.value!!.userPassword)) {
            passwordErr.value = R.string.password_err_empty
            return false
        } else if (!Validations.passwordContainsDigits(user.value!!.userPassword)) {
            passwordErr.value = R.string.password_err_digits
            return false
        } else if (!Validations.passwordContainsLowercase(user.value!!.userPassword)) {
            passwordErr.value = R.string.password_err_lowercase
            return false
        } else if (!Validations.passwordContainsUppercase(user.value!!.userPassword)) {
            passwordErr.value = R.string.password_err_uppercase
            return false
        } else if (!Validations.passwordContainsSpecialChar(user.value!!.userPassword)) {
            passwordErr.value = R.string.password_err_specialChar
            return false
        } else if (!Validations.passwordExcludesSpace(user.value!!.userPassword)) {
            passwordErr.value = R.string.password_err_whiteSpace
            return false
        } else if (!Validations.passwordLength(user.value!!.userPassword)) {
            passwordErr.value = R.string.password_err_length
            return false
        } else {
            passwordErr.value = 0
            return true
        }
    }
}