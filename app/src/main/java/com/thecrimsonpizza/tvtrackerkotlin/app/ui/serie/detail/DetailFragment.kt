package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SerieAdapter
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import kotlinx.android.synthetic.main.fragment_sinopsis.*


class DetailFragment : Fragment() {

    private val seriesViewModel: SeriesViewModel by activityViewModels()
    private lateinit var mSerie: SerieResponse.Serie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sinopsis, container, false)
        val youTubePlayerView: YouTubePlayerView = root.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seriesViewModel.getShow().observe(viewLifecycleOwner, Observer<SerieResponse.Serie>
        {
            mSerie = it
            SerieAdapter(requireContext(), requireView(), mSerie).fillOverview()
            progreso.visibility = View.GONE
        })
    }
}