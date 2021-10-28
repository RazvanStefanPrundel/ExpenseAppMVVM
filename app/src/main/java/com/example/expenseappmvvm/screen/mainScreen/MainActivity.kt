package com.example.expenseappmvvm.screen.mainScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityMainBinding
import com.example.expenseappmvvm.screen.loginScreen.LoginActivity
import com.example.expenseappmvvm.screen.mainScreen.budgetScreen.BudgetFragment
import com.example.expenseappmvvm.screen.mainScreen.expenseScreen.ExpenseFragment
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
    }

    private fun initDrawer() {
        iv_burger_menu.setOnClickListener {
            drawer.openDrawer(
                GravityCompat.START
            )
        }

        mainViewModel.logoutUser.observe(this, {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        })
    }

    private fun initObserver() {
//        mainViewModel.userName.observe(this, {
//            nav_view.getHeaderView(0).hello_msg.text = "Hello " + mainViewModel.userName
//        })
    }

    private fun initNavigation() {
        setCurrentFragment(BudgetFragment.newInstance())
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fragment_budget -> setCurrentFragment(BudgetFragment.newInstance())
                R.id.fragment_expense -> setCurrentFragment(ExpenseFragment.newInstance())
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