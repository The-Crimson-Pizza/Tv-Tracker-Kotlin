package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SerieActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_SERIE_POSTER_PATH
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.EMPTY_STRING
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import kotlinx.android.synthetic.main.fragment_serie_search.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*

class SerieSearchFragment : Fragment() {
    private val showList = mutableListOf<SerieResponse.Serie>()
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_serie_search, container, false)
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
            rvSeriesSearch.adapter?.notifyDataSetChanged()
        })
    }

    private fun setQueryListener() {
        searchViewModel.setQuery(EMPTY_STRING)
        searchViewModel.getQuery().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                if (R.id.search_image == switcherSearchSerie.nextView.id) switcherSearchSerie.showNext()
            } else {
                if (R.id.rvSeriesSearch == switcherSearchSerie.nextView.id) switcherSearchSerie.showNext()
                searchViewModel.setShowList(it)
            }
        })
    }

    private fun setAdapter() {
        rvSeriesSearch.setBaseAdapter(
            showList, R.layout.lista_series_basic,
            GridLayoutManager(activity, 3)
        ) {

            itemView.layoutParams.width = (requireView().width * 0.3).toInt()

            itemView.posterBasic.getImage(
                requireContext(), BASE_URL_IMAGES_POSTER + it.posterPath
            )
            itemView.titleBasic.text = it.name

            if (it.voteAverage > 0) itemView.ratingBasic.text = it.voteAverage.toString()
            else itemView.ratingBasic.visibility = View.GONE

            itemView.setOnClickListener { view -> goToSerieActivity(view, it) }
        }
    }

    private fun goToSerieActivity(v: View, serie: SerieResponse.Serie) {

        val intent = Intent(context, SerieActivity::class.java).apply {
            putExtras(Bundle().apply {
                putExtra(ID_SERIE, serie.id)
                putExtra(BASIC_SERIE_POSTER_PATH, serie.posterPath)
            })
        }

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            context as Activity,
            androidx.core.util.Pair(v.posterBasic, v.posterBasic.transitionName)
        )

        ActivityCompat.startActivity(requireContext(), intent, activityOptions.toBundle())

    }
}