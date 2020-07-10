package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.season.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_LONG
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SEASON
import kotlinx.android.synthetic.main.fragment_episodes.*
import kotlinx.android.synthetic.main.list_episodes.*
import kotlinx.android.synthetic.main.list_episodes.view.*


class EpisodesFragment : Fragment() {

    private var seasonPos = -1
    private var rvEpisodes: RecyclerView? = null
    private var serie: SerieResponse.Serie? = null
    private val mFavs: MutableList<SerieResponse.Serie> = mutableListOf()
    private val mEpisodes: MutableList<Episode> = mutableListOf()
    private val seriesViewModel: SeriesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seasonPos = requireArguments().getInt(ID_SEASON)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seriesViewModel.serieMutable.observe(viewLifecycleOwner, Observer<SerieResponse.Serie>
        {
            serie = it
        })

        seriesViewModel.getFollowingShows()
            ?.observe(viewLifecycleOwner, Observer<List<SerieResponse.Serie>> {
                mFavs.clear()
                mFavs.addAll(it)
                if (serie != null) {
                    serie?.checkFav(mFavs)
                    mEpisodes.clear()
                    mEpisodes.addAll(serie!!.seasons[seasonPos].episodes)
                    setAdapter()
                }
            })


    }

    private fun watchEpisode(episodePos: Int, watched: Boolean = true) {
        val pos: Int = serie?.getPosition(mFavs) ?: -1
        if (pos != -1) {
            mFavs[pos].seasons[seasonPos].episodes[episodePos].markAsWatched(watched)
            mFavs[pos].seasons[seasonPos].markAsWatched(mFavs[pos].checkEpisodesInSeasonFinished(seasonPos))
            mFavs[pos].markAsWatched(mFavs[pos].checkSeasonsFinished())
            mFavs.saveToFirebase()
        }
    }

    private fun setWatchCheck(pos: Int, episode: Episode) {
        checkbox_watched.visibility = View.VISIBLE
        checkbox_watched.isChecked = episode.watched
        checkbox_watched.setOnCheckedChangeListener { _, isChecked: Boolean ->
            watchEpisode(pos, !isChecked)
        }
    }

    private fun setAdapter() {

        gridEpisodes.setBaseAdapter(
            mEpisodes, R.layout.list_episodes,
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        ) {

            if (serie!!.added) setWatchCheck(adapterPosition, it)

            image_episode.getImage(requireContext(), it.stillPath)
            episode_name.text = it.name
            nextEpisodeNameExpandable.text = it.name
            episode_fecha.text = it.airDate.changeDateFormat(FORMAT_LONG)
            episode_sinopsis.text = it.overview.checkNull(requireContext())
            episode_time.text =
                (if (!serie?.episodeRunTime.isNullOrEmpty()) {
                    serie?.episodeRunTime?.first()?.getMinutes()
                } else 45.getMinutes())
            checkbox_watched.expandableViewEpi.arrowBtnEpi.setOnClickListener {
                TransitionManager.beginDelayedTransition(cardview, AutoTransition())
                expandableViewEpi.visibility =
                    if (expandableViewEpi.isShown) View.GONE else View.VISIBLE
                when (expandableViewEpi.visibility) {
                    View.VISIBLE -> arrowBtnEpi.setBackgroundResource(R.drawable.arrow_expand)
                    View.GONE -> arrowBtnEpi.setBackgroundResource(R.drawable.arrow_collapse)
                }
            }
        }
    }
}