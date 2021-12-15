package com.example.expenseappmvvm.screen.mainScreen

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityMainBinding
import com.example.expenseappmvvm.screen.actionScreen.ActionActivity
import com.example.expenseappmvvm.screen.loginScreen.LoginActivity
import com.example.expenseappmvvm.screen.mainScreen.budgetScreen.BudgetFragment
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.ExpenseFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_menu.*
import kotlinx.android.synthetic.main.drawer_menu.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewModel = this@MainActivity.mainViewModel
            lifecycleOwner = this@MainActivity
        }

        initDrawer()
        initNavigation()
        initObserver()
        initListeners()
    }

    private fun initDrawer() {
        mainViewModel.getUserName()
    }

    private fun initObserver() {
        mainViewModel.userName.observe(this, {
            nav_view.getHeaderView(0).hello_msg.text = "Hello $it"
        })
    }

    private fun initListeners() {
        iv_burger_menu.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
            drawer.bringToFront()
        }

        imageView_toolbar_add.setOnClickListener {
            startActivity(Intent(this, ActionActivity::class.java))
        }
//TODO
        nav_view.getHeaderView(0).textView_logout.setOnClickListener {
            mainViewModel.logoutUserOnClick()
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    private fun initNavigation() {
        setCurrentFragment(BudgetFragment())
        bottom_nav_view.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_budget -> setCurrentFragment(BudgetFragment())
                R.id.navigation_expense -> setCurrentFragment(ExpenseFragment())
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_host, fragment)
            commit()
        }

}