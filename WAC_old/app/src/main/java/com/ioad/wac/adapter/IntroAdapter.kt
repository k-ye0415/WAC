package com.ioad.wac.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ioad.wac.fragment.IntroFirstFragment
import com.ioad.wac.fragment.IntroSecondFragment

class IntroAdapter(
    fragmentActivity: FragmentActivity,
    val tabCount:Int
): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return IntroFirstFragment()
            else -> return IntroSecondFragment()
        }
    }
}