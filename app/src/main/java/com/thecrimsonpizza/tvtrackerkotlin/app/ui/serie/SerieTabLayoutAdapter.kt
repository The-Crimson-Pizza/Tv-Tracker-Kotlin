package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.home.HomeFragment
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.cast.CastFragment
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.overview.SinopsisFragment
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.season.SeasonFragment

class SerieTabLayoutAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SinopsisFragment()
            1 -> CastFragment()
            2 -> SeasonFragment()
            else -> HomeFragment()
        }
    }
}