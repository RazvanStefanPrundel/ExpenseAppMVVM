package com.example.expenseappmvvm.screen.resetScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityResetBinding
import com.example.expenseappmvvm.screen.loginScreen.LoginActivity
import kotlinx.android.synthetic.main.activity_reset.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetActivity : AppCompatActivity() {
    private val resetViewModel: ResetViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityResetBinding>(this, R.layout.activity_reset).apply {
            viewModel = this@ResetActivity.resetViewModel
            lifecycleOwner = this@ResetActivity
        }

        resetViewModel.apply {
            emailTextChanged(forgot_email_inputText)
        }

        initObserver()
    }

    private fun initObserver(){
        resetViewModel.checkStatus.observe(this, {
            if(it){
                Toast.makeText(this, resetViewModel.email.toString(), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else{
                Toast.makeText(this, getString(R.string.no_user_found), Toast.LENGTH_SHORT).show()
            }
        })
    }
}