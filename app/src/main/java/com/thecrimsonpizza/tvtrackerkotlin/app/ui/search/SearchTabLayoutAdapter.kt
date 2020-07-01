package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.home.HomeFragment

class SearchTabLayoutAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SerieSearchFragment()
            1 -> ActorSearchFragment()
            else -> HomeFragment()
        }
    }
}