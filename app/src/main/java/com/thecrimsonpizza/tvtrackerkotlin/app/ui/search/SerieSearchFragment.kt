package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImagePortrait
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.EMPTY_STRING
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import kotlinx.android.synthetic.main.fragment_serie_search.*
import kotlinx.android.synthetic.main.lista_series_basic_vertical.view.*

class SerieSearchFragment : Fragment() {
    private val showList = mutableListOf<SerieResponse.Serie>()
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this@SerieSearchFragment).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_serie_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSeriesSearch.setBaseAdapter(
            showList, R.layout.lista_series_basic_vertical,
            GridLayoutManager(activity, 3)
        ) {
            itemView.posterBasicVertical.getImagePortrait(
                requireContext(), it.posterPath.toString()
            )
            itemView.titleBasicVertical.text = it.name
            itemView.ratingBasicVertical.text = it.voteAverage.toString()
            itemView.setOnClickListener { view ->
                goToFragment(view, it.id)
            }
        }

        searchViewModel.setQuery(EMPTY_STRING)
        searchViewModel.getQuery()?.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                if (R.id.search_image == switcherSearchSerie.nextView.id) switcherSearchSerie.showNext()
            } else {
                if (R.id.rvSeriesSearch == switcherSearchSerie.nextView.id) switcherSearchSerie.showNext()
                searchViewModel.initPersonSearch(it)
            }
        })

        searchViewModel.getShowList().observe(viewLifecycleOwner, Observer {
            showList.clear()
            it.results.let { it1 -> showList.addAll(it1) }
            rvSeriesSearch.adapter?.notifyDataSetChanged()
        })
    }

    private fun goToFragment(v: View, id: Int) {
        val bundle = Bundle()
        bundle.putInt(ID_SERIE, id)
        Navigation.findNavController(v).navigate(R.id.action_search_to_series, bundle)
    }
}