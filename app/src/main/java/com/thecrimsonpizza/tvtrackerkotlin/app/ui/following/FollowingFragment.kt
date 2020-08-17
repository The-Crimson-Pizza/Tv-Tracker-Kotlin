package com.thecrimsonpizza.tvtrackerkotlin.app.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.goToBaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.translateStatus
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ES
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Util
import kotlinx.android.synthetic.main.fragment_favoritos.*
import kotlinx.android.synthetic.main.list_series_following.view.*

class FollowingFragment : Fragment() {

    private val followingList = mutableListOf<SerieResponse.Serie>()
/*    private var orientationSelected = false
    private val followingViewModel: FollowingViewModel by lazy {
        ViewModelProvider(this@FollowingFragment).get(FollowingViewModel::class.java)
    }*/

    private val followingViewModel: FollowingViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer {
            followingList.clear()
            it.data?.let { it1 -> followingList.addAll(it1) }
            grid_favoritas.adapter?.notifyDataSetChanged()
        })

        radioSortAdded.setOnClickListener {
            followingList.sortedWith(nullsLast(compareBy { it.followingData?.addedDate }))
            grid_favoritas.adapter?.notifyDataSetChanged()
        }

        radioSortAbc.setOnClickListener {
            followingList.sortedWith(nullsLast(compareBy { it.name }))
            grid_favoritas.adapter?.notifyDataSetChanged()
        }
        radioSortWatched.setOnClickListener {
//            followingList.setLastWatched()
            followingList.sortedWith(
                nullsLast(
                    compareBy { followingSerie ->
                        followingSerie.followingData?.episodesData
                            ?.flatMap { it.value }
                            ?.findLast { it.watched }?.watchedDate
                    }
                )
            )
//            followingList.sortedWith(nullsLast(compareBy { it.firstEpisodeUnwatched.followingData.watchedDate }))
            grid_favoritas.adapter?.notifyDataSetChanged()
        }

        checkboxSortDirection.setOnClickListener {
//            orientationSelected = !orientationSelected
            followingList.reverse()
            grid_favoritas.adapter?.notifyDataSetChanged()
        }
    }

    private fun setAdapter() {
        grid_favoritas.setBaseAdapter(
            followingList, R.layout.list_series_following

        ) { serie ->

//            val data = serie.getFollowingSerie()

//            serie.seasons.sortedWith(nullsLast(compareBy { season -> season.seasonNumber }))
            itemView.nameFollowing.text = serie.name
            itemView.posterFollowing.getImage(
                requireContext(),
                BASE_URL_IMAGES_POSTER + serie.posterPath
            )
            if (Util().getLanguageString() == ES) itemView.episodeFollowingDate.text =
                serie.status.translateStatus()
            else itemView.episodeFollowingDate.text = serie.status

            itemView.arrowBtn.setOnClickListener {
                TransitionManager.beginDelayedTransition(itemView.card_view, AutoTransition())
                itemView.expandable_view.visibility =
                    if (itemView.expandable_view.isShown) View.GONE else View.VISIBLE
                when (itemView.expandable_view.visibility) {
                    View.VISIBLE -> itemView.arrowBtn.setBackgroundResource(R.drawable.arrow_expand)
                    View.GONE -> itemView.arrowBtn.setBackgroundResource(R.drawable.arrow_collapse)
                }
            }
            itemView.setOnClickListener {
                serie.goToBaseActivity(requireContext(), itemView.posterFollowing)
            }

            if (serie.followingData?.finished == true) itemView.btWatchEpisode.visibility = View.GONE
            itemView.btWatchEpisode.setOnClickListener {
                serie.followingData?.watchEpisode().also {
                    // todo - get ultimo episodio visto y primero sin ver
                }

                if (serie.followingData?.finishedDate != null) {
                    itemView.btWatchEpisode.visibility = View.GONE
                }
            }

            // PROGRESS
            itemView.watchedEpisodesFollowing.text =
                context?.getString(
                    R.string.num_vistos,
                    serie.followingData?.countEpisodesWatched()?:0,
                    serie.numberOfEpisodes
                )
            itemView.followingProgress.progress = serie.followingData?.getProgress(serie) ?: 0

            val last: Episode? = serie.firstEpisodeUnwatched

            val name = last?.getUnwatchedTitle(requireContext(), serie)
            itemView.nextEpisodeNameExpandable.text = name
            itemView.nextEpisodeName.text = name
            itemView.sinopsis.text = last?.getUnwatchedOverview(requireContext(), serie)

        }
    }
}