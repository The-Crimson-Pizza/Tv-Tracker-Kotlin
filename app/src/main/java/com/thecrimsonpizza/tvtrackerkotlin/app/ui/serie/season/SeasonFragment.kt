package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.season

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SEASON
import kotlinx.android.synthetic.main.fragment_seasons.*
import kotlinx.android.synthetic.main.list_season.*

class SeasonFragment : Fragment() {

    private var serie: SerieResponse.Serie? = null
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
        gridSeasons.setBaseAdapter(
            serie!!.seasons, R.layout.list_season,
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        ) {
            if (serie!!.added) setWatchCheck(adapterPosition, it)

            image_season.getImage(requireContext(), it.posterPath)
            season_name.text = it.name
            episode_number
            checkbox_watched

            if (!it.episodes.isNullOrEmpty()) {
                if (serie!!.added)
                    episode_number.text =
                        requireContext().resources.getQuantityString(
                            R.plurals.num_episodes_follow,
                            it.episodes.size,
                            serie?.countEpisodesWatched(),
                            it.episodes.size
                        )
                else
                    episode_number.text = requireContext().resources.getQuantityString(
                        R.plurals.n_episodes,
                        it.episodes.size,
                        it.episodes.size
                    )
            } else episode_number.text = requireContext().getString(R.string.no_data)

            itemView.setOnClickListener { goToEpisodes(adapterPosition) }
        }
    }

    private fun getFollowingSeries() {
        seriesViewModel.getFollowingShows()?.observe(viewLifecycleOwner, Observer {
            followingList.clear()
            followingList.addAll(it)
            if (serie != null) {
                serie!!.checkFav(followingList)
            }
            gridSeasons.adapter?.notifyDataSetChanged()
        })
    }

    private fun getSerie() {
        seriesViewModel.serieMutable.observe(viewLifecycleOwner, Observer {
            serie = it
            setAdapter()
        })
    }

    private fun goToEpisodes(pos: Int) {
        val bundle = Bundle()
        bundle.putInt(ID_SEASON, pos)
        Navigation.findNavController(requireView()).navigate(R.id.action_series_to_episodes, bundle)
    }

    private fun watchSeason(posSeason: Int, watched: Boolean = true) {
        val pos: Int = serie?.getPosition(followingList) ?: -1
        if (pos != -1) {
            followingList[pos].seasons[posSeason].markAsWatched(watched)
            followingList[pos].markAsWatched(followingList[pos].checkSeasonsFinished())
            followingList.saveToFirebase()
        }
    }

    private fun setWatchCheck(pos: Int, season: Season) {
        checkbox_watched.visibility = View.VISIBLE
        checkbox_watched.isChecked = season.watched
        checkbox_watched.setOnCheckedChangeListener { _, isChecked: Boolean ->
            watchSeason(pos, !isChecked)
        }
    }
}