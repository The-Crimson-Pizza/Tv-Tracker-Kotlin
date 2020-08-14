package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.season

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.season.episode.EpisodesFragment
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
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

            if (serie.followingData.added) setWatchCheck(itemView, adapterPosition, it)

            itemView.image_season.getImage(requireContext(), BASE_URL_IMAGES_POSTER + it.posterPath)
            itemView.season_name.text = it.name

            if (!it.episodes.isNullOrEmpty()) {
                if (serie.followingData.added)
                    itemView.episode_number.text =
                        requireContext().resources.getQuantityString(
                            R.plurals.num_episodes_follow,
                            it.episodes.size,
                            serie.countEpisodesWatched(),
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
            followingList.addAll(it)
            serie.checkFav(followingList)
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
            followingList[pos].seasons[posSeason].markAsWatched(watched)
            followingList[pos].markAsWatched(followingList[pos].checkSeasonsFinished())
            followingList.saveToFirebase()
        }
    }

    private fun setWatchCheck(itemView: View, pos: Int, season: Season) {
        if (!season.episodes.isNullOrEmpty()) {
            itemView.checkbox_watched.visibility = View.VISIBLE
        }
        itemView.checkbox_watched.isChecked = season.followingData.watched
        itemView.checkbox_watched.setOnCheckedChangeListener { _, isChecked: Boolean ->
            watchSeason(pos, !isChecked)
        }
    }
}