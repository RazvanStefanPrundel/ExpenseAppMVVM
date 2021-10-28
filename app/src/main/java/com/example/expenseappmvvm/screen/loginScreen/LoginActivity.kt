package com.example.expenseappmvvm.screen.loginScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityLoginBinding
import com.example.expenseappmvvm.screen.actionScreen.ActionActivity
import com.example.expenseappmvvm.screen.mainScreen.MainActivity
import com.example.expenseappmvvm.screen.registerScreen.RegisterActivity
import com.example.expenseappmvvm.screen.resetScreen.ResetActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login).apply {
            viewModel = this@LoginActivity.loginViewModel
            lifecycleOwner = this@LoginActivity
        }

        loginViewModel.apply {
            emailTextChanged(login_email_inputText)
            passwordTextChanged(login_pass_inputText)
        }

        initObservers()
    }

    private fun initObservers() {
        loginViewModel.redirectToRegister.observe(this, {
            startActivity(Intent(this, RegisterActivity::class.java))
        })

        loginViewModel.redirectToReset.observe(this, {
            startActivity(Intent(this, ResetActivity::class.java))
        })

        loginViewModel.redirectToMain.observe(this, {
            startActivity(Intent(this, ActionActivity::class.java))
        })

        loginViewModel.loginStatus.observe(this, {
            if (it){
                Toast.makeText(this, getString(R.string.loggin_success), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.register_failed), Toast.LENGTH_SHORT).show()
            }
        })

    }

}