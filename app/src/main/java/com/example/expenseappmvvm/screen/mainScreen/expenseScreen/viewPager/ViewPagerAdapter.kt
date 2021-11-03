package com.example.expenseappmvvm.screen.mainScreen.expenseScreen.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ViewPagerFragment.newInstance(WEEK_TAB)
            1 -> ViewPagerFragment.newInstance(MONTH_TAB)
            else -> ViewPagerFragment.newInstance(YEAR_TAB)
        }
    }

    companion object {
        private const val NUM_TABS = 3

         const val WEEK_TAB = "WEEK_TAB"
         const val MONTH_TAB = "MONTH_TAB"
         const val YEAR_TAB = "YEAR_TAB"
    }

}