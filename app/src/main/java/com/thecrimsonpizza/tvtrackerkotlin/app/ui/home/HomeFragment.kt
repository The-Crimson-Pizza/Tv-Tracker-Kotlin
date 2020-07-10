package com.thecrimsonpizza.tvtrackerkotlin.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val trendList = mutableListOf<BasicResponse.SerieBasic>()
    private val newList = mutableListOf<BasicResponse.SerieBasic>()
    private val followingList = mutableListOf<SerieResponse.Serie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBaseAdapter(gridTrend, trendList)
        setBaseAdapter(gridNew, newList)
        setFollowingAdapter(gridFollowing, followingList)


/*
  homeViewModel.getFollowingShows()?.observe(viewLifecycleOwner, Observer {
            refreshData(followingList, it, gridFollowing)
        }) */

        homeViewModel.getNewShows().observe(viewLifecycleOwner, Observer {
            refreshData(newList, it.basicSeries, gridNew)
        })

        homeViewModel.getTrendingShows().observe(viewLifecycleOwner, Observer {
            refreshData(trendList, it.basicSeries, gridTrend)
        })


    }

    private fun <T : Any> refreshData(
        list: MutableList<T>, response: List<T>, recycler: RecyclerView
    ) {
        list.clear()
        list.addAll(response)
        recycler.adapter?.notifyDataSetChanged()
    }

    private fun setBaseAdapter(recycler: RecyclerView, list: List<BasicResponse.SerieBasic>) {
        recycler.setBaseAdapter(
            list,
            R.layout.lista_series_basic,
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        ) { show ->
            itemView.posterBasic.getImage(requireContext(), BASE_URL_IMAGES_POSTER + show.posterPath)
            itemView.titleBasicTutorial.text = show.name
            itemView.ratingBasic.text = show.voteAverage.toString()
            itemView.setOnClickListener { goToSerieFragment(it, it.id) }
        }
    }

    private fun setFollowingAdapter(recycler: RecyclerView, list: List<SerieResponse.Serie>) {
        recycler.setBaseAdapter(
            list,
            R.layout.lista_series_basic,
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        ) { show ->
            itemView.posterBasic.getImage(requireContext(), BASE_URL_IMAGES_POSTER +show.posterPath.toString())
            itemView.titleBasicTutorial.text = show.name
            itemView.ratingBasic.text = show.voteAverage.toString()
            itemView.setOnClickListener { goToSerieFragment(it, it.id) }
        }
    }

    private fun goToSerieFragment(view: View, id: Int) {
        val bundle = Bundle()
        bundle.putInt(ID_SERIE, id)
        Navigation.findNavController(view).navigate(R.id.action_home_to_series, bundle)
    }
}