package com.example.expenseappmvvm.screen.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivitySplashBinding
import com.example.expenseappmvvm.screen.actionScreen.ActionActivity
import com.example.expenseappmvvm.screen.loginScreen.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash).apply {
            viewModel = this@SplashActivity.splashViewModel
            lifecycleOwner = this@SplashActivity
        }
        startAnimation()
        redirectAfterAnim()
        initObserver()
    }

    private fun initObserver(){
        splashViewModel.autoLogin.observe(this, {
            startActivity(Intent(this, ActionActivity::class.java))
        })
    }

    private fun startAnimation() {
        val imageAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.animation_image)
        val textAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.animation_text)

        splash_img.animation = imageAnim
        splash_tv.animation = textAnim
    }

    private fun redirectAfterAnim() {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                splashViewModel.verifyAutoLogin()
                startActivity(Intent(this, LoginActivity::class.java))
            }, 2000)
        }
    }
}