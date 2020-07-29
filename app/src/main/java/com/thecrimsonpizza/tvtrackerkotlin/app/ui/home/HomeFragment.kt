package com.thecrimsonpizza.tvtrackerkotlin.app.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.goToBaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapterTwo
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*


class HomeFragment : Fragment() {

    val TAG: String? = this::class.simpleName

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val followingViewModel: FollowingViewModel by activityViewModels()

    private val trendList = mutableListOf<BasicResponse.SerieBasic>()
    private val newList = mutableListOf<BasicResponse.SerieBasic>()
    private val followingList = mutableListOf<SerieResponse.Serie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel.init()
        followingViewModel.init()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBaseAdapter(gridTrend, trendList)
        setBaseAdapter(gridNew, newList)
        setBaseAdapter(gridFollowing, followingList)

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                if (gridFollowing.id == switcher_favs.nextView.id) switcher_favs.showNext()
                refreshData(followingList, it, gridFollowing)
            } else if (no_data_favs.id == switcher_favs.nextView.id) switcher_favs.showNext()
        })

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
        recycler.setBaseAdapterTwo(list, R.layout.lista_series_basic) { show ->
            itemView.posterBasic.getImage(
                requireContext(), BASE_URL_IMAGES_POSTER + show.posterPath.toString()
            )
            itemView.titleBasic.text = show.name
            itemView.ratingBasic.text = show.voteAverage.toString()
            itemView.setOnClickListener { show.goToBaseActivity(requireContext(), it) }
        }
    }
}