package com.example.expenseappmvvm.screen.registerScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityRegisterBinding
import com.example.expenseappmvvm.screen.loginScreen.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
            .apply {
                viewModel = this@RegisterActivity.registerViewModel
                lifecycleOwner = this@RegisterActivity
            }

        observeLiveData()
    }

    private fun observeLiveData() {
        registerViewModel.goToLoginScreen.observe(this, {
            LoginActivity.startLogin(this)
        })
    }

    companion object {
        fun startRegister(activity: Activity) {
            val intent = Intent(activity, RegisterActivity::class.java)
            activity.startActivity(intent)
        }
    }
}