package com.example.expenseappmvvm.screen.loginScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityLoginBinding
import com.example.expenseappmvvm.screen.registerScreen.RegisterActivity
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login).apply {
            viewModel = this@LoginActivity.loginViewModel
            lifecycleOwner = this@LoginActivity
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        loginViewModel.goToRegisterScreen.observe(this, {
            RegisterActivity.startRegister(this)
        })
    }

    companion object {
        fun startLogin(activity: Activity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }
}