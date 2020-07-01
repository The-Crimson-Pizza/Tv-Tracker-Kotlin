package com.thecrimsonpizza.tvtrackerkotlin.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.scales.OrdinalColor
import com.google.firebase.auth.FirebaseAuth
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.login.LoginActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.EMPTY_STRING
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private val followingList = mutableListOf<SerieResponse.Serie>()
    private val COLORS = arrayOf(
        "#48c9b0",
        "#2DAEA9",
        "#23949C",
        "#28798A",
        "#2E6072",
        "#2F4858",
        "#00B7C3",
        "#00A2D2",
        "#0089D5",
        "#446BC5"
    )


    private val followingViewModel: FollowingViewModel by lazy {
        ViewModelProvider(this@ProfileFragment).get(FollowingViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        email.text = FirebaseAuth.getInstance().currentUser?.email ?: EMPTY_STRING

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            requireActivity().finish()
        }

        followingViewModel.followingMutable.observe(viewLifecycleOwner, Observer {
            followingList.clear()
            followingList.addAll(it)
            initGenrePieChart()
            initNetworkTagCloud()
            fillStats()
        })
    }

    private fun initGenrePieChart() {
        APIlib.getInstance().setActiveAnyChartView(pie_chart)
        pie_chart.setBackgroundColor("#00000000")
        pie_chart.setProgressBar(progress_pie)
        val pie = AnyChart.pie()
        val data: List<DataEntry> =
            StatsAdapter(requireContext()).getGenresChartData(followingList)
        pie.data(data)
        pie.title().enabled(false)
        pie.labels().fontSize(15)
        pie.labels().fontColor("white")
        pie.labels().fontWeight(700)
        pie.legend().enabled(false)
        pie.palette(COLORS)
        pie.background().fill("rgba(0,0,255,0)")
        pie_chart.setChart(pie)
    }


    private fun initNetworkTagCloud() {
        APIlib.getInstance().setActiveAnyChartView(bubble_chart)
        bubble_chart.setBackgroundColor("#00000000")
        bubble_chart.setProgressBar(progress_bubble)
        val tagCloud = AnyChart.tagCloud()
        tagCloud.title().enabled(false)
        tagCloud.legend().enabled(false)
        val ordinalColor = OrdinalColor.instantiate()
        ordinalColor.colors(
            arrayOf(
                "#26959f", "#f18126", "#3b8ad8", "#60727b", "#e24b26"
            )
        )
        tagCloud.colorScale(ordinalColor)
        tagCloud.angles(arrayOf(-90.0, 0.0, 90.0))
        tagCloud.colorRange().enabled(false)
        tagCloud.normal().fontFamily("Arial")
        val data: List<DataEntry> =
            StatsAdapter(requireContext()).getNetworksChartData(followingList)
        tagCloud.background().fill("rgba(0,0,255,0)")
        tagCloud.data(data)
        bubble_chart.setChart(tagCloud)
    }

    private fun fillStats() {
        total_time.text = StatsAdapter(requireContext()).countTimeEpisodesWatched(followingList)
        num_series.text = StatsAdapter(requireContext()).countSeries(followingList)
        eps_vistos.text =
            StatsAdapter(requireContext()).countTotalEpisodesWatched(followingList)
        serie_max.text = StatsAdapter(requireContext()).mostWatchedTvShow(followingList)
    }
}