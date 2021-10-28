package com.example.expenseappmvvm.screen.registerScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityRegisterBinding
import com.example.expenseappmvvm.screen.actionScreen.ActionActivity
import com.example.expenseappmvvm.screen.loginScreen.LoginActivity
import com.example.expenseappmvvm.screen.splashScreen.SplashViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterActivity : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
            .apply {
                viewModel = this@RegisterActivity.registerViewModel
                lifecycleOwner = this@RegisterActivity
            }

        registerViewModel.apply {
            nameTextChanged(register_name_inputText)
            emailTextChanged(register_email_inputText)
            passwordTextChanged(register_pass_inputText)
        }

        initObservers()
    }

    private fun initObservers() {
        registerViewModel.autologin.observe(this, {
            startActivity(Intent(this, ActionActivity::class.java))
        })

        registerViewModel.redirectToLogin.observe(this, {
            startActivity(Intent(this, LoginActivity::class.java))
        })

        registerViewModel.registerStatus.observe(this, {
            if (it) {
                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
            }
        })
    }
}