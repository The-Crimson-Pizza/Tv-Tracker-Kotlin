package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.goToBaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.EMPTY_STRING
import kotlinx.android.synthetic.main.detail_fragment_search.*
import kotlinx.android.synthetic.main.list_series_basic.view.*

class SerieSearchFragment : Fragment() {
    private val showList = mutableListOf<SerieResponse.Serie>()
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setQueryListener()
        setSeriesByQueryListener()
    }

    private fun setSeriesByQueryListener() {
        searchViewModel.getShowList().observe(viewLifecycleOwner, Observer { serieResponse ->
            showList.clear()
            serieResponse.results.let { showList.addAll(it) }
            rvSearch.adapter?.notifyDataSetChanged()
        })
    }

    private fun setQueryListener() {
        searchViewModel.setQuery(EMPTY_STRING)
        searchViewModel.getQuery().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                if (R.id.search_image == switcherSearch.nextView.id) switcherSearch.showNext()
            } else {
                if (R.id.rvSearch == switcherSearch.nextView.id) switcherSearch.showNext()
                searchViewModel.setShowList(it)
            }
        })
    }

    private fun setAdapter() {
        rvSearch.setBaseAdapter(
            showList, R.layout.list_series_basic

        ) { serie ->

            itemView.layoutParams.width = (requireView().width * 0.3).toInt()

            itemView.posterBasic.getImage(
                requireContext(), BASE_URL_IMAGES_POSTER + serie.posterPath
            )
            itemView.titleBasic.text = serie.name

            if (serie.voteAverage > 0) itemView.ratingBasic.text = serie.voteAverage.toString()
            else itemView.ratingBasic.visibility = View.GONE

            itemView.setOnClickListener { serie.goToBaseActivity(requireContext(),  itemView.posterBasic) }
        }
    }
}