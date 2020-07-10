package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.core.view.forEachIndexed
import androidx.navigation.Navigation
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_YEAR
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_GENRE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_NETWORK
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Util
import kotlinx.android.synthetic.main.fragment_serie.view.*
import kotlinx.android.synthetic.main.fragment_sinopsis.view.*

class SerieAdapter(val context: Context, val view: View, private val serie: SerieResponse.Serie) {

    fun fillCollapseBar() {
        fillBasics()
        fillImages()
    }

    /**
     * Fills the overview and calls the methods that load genres, networks and trailer
     */
    fun fillOverview() {
        view.seguimiento.visibility = if (serie.added) View.VISIBLE else View.GONE
        view.sinopsis_text.text = serie.overview.checkNull(context)
        fillGenres()
        fillNetworks()
        fillTrailer()
    }

    /**
     * Sets the youtube viewer and load the trailer
     */
    private fun fillTrailer() {
        if (serie.video != null) {
            view.youtube_player_view.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(serie.video!!.key, 0f)
                    view.youtube_player_view.visibility = View.VISIBLE
                }
            })
        } else view.trailerContainer.visibility = View.GONE
    }

    /**
     * Fills the Networks images and sets the click listener
     */
    private fun fillNetworks() {
        if (serie.networks != null) {
            val networksList = mutableListOf<SerieResponse.Serie.Network>()
            for (a in 0..2) {
                serie.networks?.getOrNull(a)?.let { networksList.add(it) }
            }
            view.networks.forEachIndexed { index, view ->
                if (index > networksList.size) {
                    networksList[index].logoPath?.let {
                        (view as ImageButton).getImageNoPlaceholder(
                            context, it
                        )
                    }
                    view.visibility = View.VISIBLE
                    view.setOnClickListener { goToNetworkFragment(index, it) }
                }
            }
        } else {
            if (R.id.networks == view.switcher_networks.nextView
                    .id || R.id.no_data_networks == view.switcher_networks.nextView.id
            ) view.switcher_networks.showNext()
        }
    }

    /**
     * Fills the Genres buttons and sets the click listener
     */
    private fun fillGenres() {
        if (serie.genres != null) {
            val genresList = mutableListOf<SerieResponse.Serie.Genre>()
            for (a in 0..2) {
                serie.genres?.getOrNull(a)?.let { genresList.add(it) }
            }
            view.genres.forEachIndexed { index, view ->
                if (index > genresList.size) {
                    (view as Button).text = genresList[index].name
                    view.visibility = View.VISIBLE
                    view.setOnClickListener { goToGenreFragment(index, it) }
                }
            }
        } else {
            if (R.id.genres == view.switcher_genres.nextView
                    .id || R.id.no_data_genres == view.switcher_genres.nextView.id
            ) view.switcher_genres.showNext()
        }
    }


    /**
     * Fills all the images in the fragment
     */
    private fun fillImages() {
        view.posterImage.getImage(context, serie.posterPath.toString())
        view.imagen_background.getImageNoPlaceholder(context, serie.backdropPath.toString())
    }

    /**
     * Fill the basic serie info
     */
    private fun fillBasics() {
        view.fechaSerie.text = serie.firstAirDate?.changeDateFormat(FORMAT_YEAR)
        view.country_serie.text =
            if (serie.originCountry!!.isNotEmpty()) serie.originCountry?.get(0) else context.getString(
                R.string.no_data
            )
        if (Util().getLanguageString() == "es-ES") {
            view.status_serie.text = serie.status.translateStatus()
        } else {
            view.status_serie.text = serie.status
        }
        view.toolbar_layout.title = serie.name
    }


    /**
     * Sets the data to be sent to NetworkFragment and calls it
     *
     * @param pos position of the list
     * @param v   view of the fragment
     */
    private fun goToNetworkFragment(pos: Int, v: View) {
        val bundle = Bundle()
        bundle.putParcelable(ID_NETWORK, serie.networks?.get(pos))
        Navigation.findNavController(v)
            .navigate(R.id.action_navigation_series_to_networkFragment, bundle)
    }

    /**
     * Sets the data to be sent to GenreFragment and calls it
     *
     * @param pos position of the list
     * @param v   view of the fragment
     */
    private fun goToGenreFragment(pos: Int, v: View) {
        val bundle = Bundle()
        bundle.putParcelable(ID_GENRE, serie.genres?.get(pos))
        Navigation.findNavController(v)
            .navigate(R.id.action_navigation_series_to_genreFragment, bundle)
    }
}

