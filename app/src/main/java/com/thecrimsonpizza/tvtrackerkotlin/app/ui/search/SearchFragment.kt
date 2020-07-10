package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.thecrimsonpizza.tvtrackerkotlin.R
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()
    var stringSerie = ""
    var stringPerson = ""
    var pos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager(view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) searchViewModel.setQuery(query)
                else searchViewModel.setQuery(getString(R.string.empty))
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (pos == 0) stringSerie = query else stringPerson = query
                if (query.isNotEmpty()) searchViewModel.setQuery(query)
                else searchViewModel.setQuery(getString(R.string.empty))
                return true
            }
        })
    }

    private fun setViewPager(view: View) {
        view_pager.adapter = SearchTabLayoutAdapter(this)
        val tabs = arrayOf(getString(R.string.series), getString(R.string.people))
        val tabLayout: TabLayout = view.findViewById(R.id.tabs)
        TabLayoutMediator(
            tabLayout, view_pager,
            TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = tabs[position]
            }
        ).attach()


        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                searchView.requestFocus()
                pos = position
                if (position == 0) {
                    searchView.setQuery(stringSerie, false)

                } else {
                    searchView.setQuery(stringPerson, false)
                }

//                val imm: InputMethodManager? =
//                    getSystemService<Any>(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        })
    }
}