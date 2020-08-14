package com.thecrimsonpizza.tvtrackerkotlin.app.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.ConnectionLiveData
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.goToBaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Status
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_series_basic.view.*


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
        followingViewModel.init()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBaseAdapter(gridTrend, trendList)
        setBaseAdapter(gridNew, newList)
        setBaseAdapter(gridFollowing, followingList)

        val message =
            Snackbar.make(requireView(), getString(R.string.no_conn), Snackbar.LENGTH_INDEFINITE)

        val connectionLiveData = ConnectionLiveData(requireContext())
        connectionLiveData.observe(viewLifecycleOwner, Observer { isConnected ->
            isConnected?.let {
                if (it) {
                            getFollowingShows()
                    getNewShows()
                    getTrendingShows()
                    message.dismiss()
                } else {
                    message.show()
                }
            }
        })
    }

    private fun getTrendingShows() {
        homeViewModel.trendMutable().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> progressTrend.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    refreshData(trendList, resource.data?.basicSeries, gridTrend)
                    progressTrend.visibility = View.GONE
                }
                Status.ERROR ->
                    Snackbar.make(
                        requireView(),
                        getString(R.string.no_conn),
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
            }
        })
    }

    private fun getNewShows() {
        homeViewModel.newMutable().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> progressNew.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    progressNew.visibility = View.GONE
                    refreshData(newList, it.data?.basicSeries, gridNew)
                }
                Status.ERROR ->
                    Snackbar.make(
                        requireView(),
                        getString(R.string.no_conn),
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
            }
        })
    }

    private fun getFollowingShows() {
        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                if (gridFollowing.id == switcher_favs.nextView.id) switcher_favs.showNext()
                refreshData(followingList, it, gridFollowing)
            } else if (no_data_favs.id == switcher_favs.nextView.id) switcher_favs.showNext()
        })
    }

    private fun <T : Any> refreshData(
        list: MutableList<T>, response: List<T>?, recycler: RecyclerView
    ) {
        list.clear()
        response?.let { list.addAll(it) }
        recycler.adapter?.notifyDataSetChanged()
    }

    private fun setBaseAdapter(recycler: RecyclerView, list: List<BasicResponse.SerieBasic>) {
        recycler.setBaseAdapter(list, R.layout.list_series_basic) { show ->
            itemView.posterBasic.getImage(
                requireContext(), BASE_URL_IMAGES_POSTER + show.posterPath.toString()
            )
            itemView.titleBasic.text = show.name
            itemView.ratingBasic.text = show.voteAverage.toString()
            itemView.setOnClickListener { show.goToBaseActivity(requireContext(), it) }
        }
    }
}