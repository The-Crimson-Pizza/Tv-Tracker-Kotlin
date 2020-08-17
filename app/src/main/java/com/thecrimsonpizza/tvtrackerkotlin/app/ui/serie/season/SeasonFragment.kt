package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.season

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.FirebaseDatabaseRealtime
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.season.episode.EpisodesFragment
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SEASON
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Status
import kotlinx.android.synthetic.main.fragment_seasons.*
import kotlinx.android.synthetic.main.list_season.view.*

class SeasonFragment : Fragment() {

    private lateinit var serie: SerieResponse.Serie
    private val followingList: MutableList<SerieResponse.Serie> = mutableListOf()
    private val seriesViewModel: SeriesViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seasons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSerie()
        getFollowingSeries()
    }

    private fun setAdapter() {
        val sortedSeasons = serie.seasons.sortedBy { it.seasonNumber }
        gridSeasons.setBaseAdapter(
            sortedSeasons, R.layout.list_season
        ) {

            if (serie.followingData?.added == true && !it.episodes.isNullOrEmpty()) setWatchCheck(itemView, adapterPosition, it)
            else itemView.checkbox_watched.visibility = View.GONE

            itemView.image_season.getImage(requireContext(), BASE_URL_IMAGES_POSTER + it.posterPath)
            itemView.season_name.text = it.name

            if (!it.episodes.isNullOrEmpty()) {
                if (serie.followingData?.added == true)
                    itemView.episode_number.text =
                        requireContext().resources.getQuantityString(
                            R.plurals.num_episodes_follow,
                            it.episodes.size,
                            serie.followingData?.countEpisodesWatched() ?: 0,
                            it.episodes.size
                        )
                else itemView.episode_number.text = requireContext().resources.getQuantityString(
                    R.plurals.n_episodes,
                    it.episodes.size,
                    it.episodes.size
                )
                itemView.setOnClickListener { goToEpisodes(adapterPosition) }
            } else itemView.episode_number.text = requireContext().getString(R.string.no_data)

        }
    }

    private fun getFollowingSeries() {
        seriesViewModel.getFollowingShows()?.observe(viewLifecycleOwner, Observer {
            followingList.clear()
            it.data?.let { temp -> followingList.addAll(temp) }
//            serie.checkFav(followingList)
            gridSeasons.adapter?.notifyDataSetChanged()
        })
    }

    private fun getSerie() {
        seriesViewModel.getShow().observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                serie = it.data!!
                setAdapter()
            }
        })
    }

    private fun goToEpisodes(pos: Int) {

        val fragment = EpisodesFragment().apply {
            arguments = Bundle().apply { putInt(ID_SEASON, pos) }
        }
        fragment.show(parentFragmentManager, EpisodesFragment.TAG)

    }

    private fun watchSeason(posSeason: Int, watched: Boolean = true) {
        val pos: Int = serie.getPosition(followingList)
        if (pos != -1) {
//            followingList[pos].seasons[posSeason].markAsWatched(watched)
//            followingList[pos].markAsWatched(followingList[pos].checkSeasonsFinished())
            FirebaseDatabaseRealtime.saveToFirebase(followingList)
        }
    }

    private fun setWatchCheck(itemView: View, pos: Int, season: Season) {
//        if (!season.episodes.isNullOrEmpty()) {
            itemView.checkbox_watched.visibility = View.VISIBLE

            itemView.checkbox_watched.isChecked = checkSeasonWatched(season.seasonNumber)
            itemView.checkbox_watched.setOnCheckedChangeListener { _, isChecked: Boolean ->
                watchSeason(pos, !isChecked)
            }
//        }
    }

    private fun checkSeasonWatched(pos: Int): Boolean {
        val serieData = serie.getSerieFromFollowingList(followingList)?.followingData
        if (serieData != null) {
            return serieData.checkEpisodesFromSeason(pos)
        }
        return false
    }
}