package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.season.episode

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.FirebaseDatabaseRealtime
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Episode
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_LONG
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SEASON
import kotlinx.android.synthetic.main.fragment_episodes.*
import kotlinx.android.synthetic.main.list_episodes.view.*


class EpisodesFragment : BottomSheetDialogFragment() {


    companion object {
        const val TAG = "ActionBottomDialog"
    }

    private var seasonPos = -1
    private lateinit var serie: SerieResponse.Serie
    private val followingList: MutableList<SerieResponse.Serie> = mutableListOf()
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
//        dismissAllowingStateLoss()

        seriesViewModel.getShow().observe(viewLifecycleOwner, Observer
        {
            serie = it.data!!
            mEpisodes.clear()
            mEpisodes.addAll(serie.seasons[seasonPos].episodes)
            gridEpisodes.adapter?.notifyDataSetChanged()
        })

        seriesViewModel.getFollowingShows()
            ?.observe(viewLifecycleOwner, Observer {
                followingList.clear()
                it.data?.let { temp -> followingList.addAll(temp) }
                if (serie != null) {
//                    serie.checkFav(followingList)
                    mEpisodes.clear()
                    mEpisodes.addAll(serie.seasons[seasonPos].episodes)
                    gridEpisodes.adapter?.notifyDataSetChanged()
                }
            })

        setAdapter()
    }

    private fun watchEpisode(episodePos: Int, watched: Boolean = true) {
        val pos: Int = serie.getPosition(followingList)
//        mFavs[pos].seasons[seasonPos].episodes[episodePos].markAsWatched(watched)
//        mFavs[pos].seasons[seasonPos].markAsWatched(
//            mFavs[pos].checkEpisodesInSeasonFinished(
//                seasonPos
//            )
//        )
//        mFavs[pos].markAsWatched(mFavs[pos].checkSeasonsFinished())
        FirebaseDatabaseRealtime.saveToFirebase(followingList)
    }

    private fun setWatchCheck(itemView: View, pos: Int, episode: Episode) {
        itemView.checkbox_watched.visibility = View.VISIBLE
//        itemView.checkbox_watched.isChecked = episode.followingData.watched
        itemView.checkbox_watched.setOnCheckedChangeListener { _, isChecked: Boolean ->
            watchEpisode(pos, !isChecked)
        }
    }

    private fun setAdapter() {

        gridEpisodes.setBaseAdapter(
            mEpisodes, R.layout.list_episodes
        ) {

            if (serie.followingData?.added == true) setWatchCheck(itemView, adapterPosition, it)

            itemView.image_episode.getImage(requireContext(), BASE_URL_IMAGES_POSTER + it.stillPath)
            itemView.episode_name.text = it.name
            itemView.nextEpisodeNameExpandable.text = it.name
            itemView.episode_fecha.text =
                if (it.airDate != null) it.airDate?.changeDateFormat(FORMAT_LONG) else getString(R.string.no_data)
            itemView.episode_sinopsis.text = it.overview.checkNull(requireContext())
            itemView.episode_time.text =
                (if (!serie.episodeRunTime.isNullOrEmpty()) {
                    serie.episodeRunTime.first().getMinutes()
                } else 45.getMinutes())
            itemView.arrowBtnEpi.setOnClickListener {
//                TransitionManager.beginDelayedTransition(itemView.cardview, AutoTransition())
                itemView.expandableViewEpi.visibility =
                    if (itemView.expandableViewEpi.isShown) View.GONE else View.VISIBLE
                when (itemView.expandableViewEpi.visibility) {
                    View.VISIBLE -> itemView.arrowBtnEpi.setBackgroundResource(R.drawable.arrow_expand)
                    View.GONE -> itemView.arrowBtnEpi.setBackgroundResource(R.drawable.arrow_collapse)
                }
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}