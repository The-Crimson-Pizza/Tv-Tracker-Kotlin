package com.thecrimsonpizza.tvtrackerkotlin.app.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ES
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
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

        followingViewModel.followingMutable.observe(viewLifecycleOwner, Observer {
            followingList.clear()
            followingList.addAll(it)
            grid_favoritas.adapter?.notifyDataSetChanged()
        })

        radioSortAdded.setOnClickListener {
            followingList.sortedWith(nullsLast(compareBy { it.addedDate }))
            grid_favoritas.adapter?.notifyDataSetChanged()
        }

        radioSortAbc.setOnClickListener {
            followingList.sortedWith(nullsLast(compareBy { it.name }))
            grid_favoritas.adapter?.notifyDataSetChanged()
        }
        radioSortWatched.setOnClickListener {
            followingList.setLastWatched()
            followingList.sortedWith(nullsLast(compareBy { it.lastEpisodeWatched?.watchedDate }))
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
            followingList, R.layout.list_series_following,
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        ) { serie ->

            serie.seasons.sortedWith(nullsLast(compareBy { season -> season.seasonNumber }))
            itemView.nameFollowing.text = serie.name
            itemView.posterFollowing.getImage(requireContext(), BASE_URL_IMAGES_POSTER + serie.posterPath)
            if (Util().getLanguageString() == ES) itemView.episodeFollowingDate.text =
                serie.status?.translateStatus()
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
            //posterFollowing
            itemView.setOnClickListener { v: View ->
                val bundle = Bundle()
                bundle.putInt(ID_SERIE, serie.id)
                Navigation.findNavController(v)
                    .navigate(R.id.action_navigation_fav_to_navigation_series, bundle)
            }

            if (serie.finished) itemView.next_episode_main_menu.visibility = View.GONE
            itemView.next_episode_main_menu.setOnClickListener {
                serie.watchEpisode(followingList, true)
                if (serie.finishDate != null) {
                    itemView.next_episode_main_menu.visibility = View.GONE
                }
            }

            itemView.watchedEpisodesFollowing.text =
                context?.getString(
                    R.string.num_vistos,
                    serie.countEpisodesWatched(),
                    serie.numberOfEpisodes
                )
            itemView.followingProgress.progress = serie.getProgress()

            val last: Episode? = serie.getLastUnwatched()
            itemView.nextEpisodeNameExpandable.text = last?.getUnwatchedTitle(requireContext(), serie)
            itemView.nextEpisodeName.text = last?.getUnwatchedTitle(requireContext(), serie)
            itemView.sinopsis.text = last?.getUnwatchedOverview(requireContext(), serie)

        }
    }
}