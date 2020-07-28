package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.Navigation
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_BACK
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_NETWORK
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_YOUTUBE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_YEAR
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_GENRE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_NETWORK
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Util
import kotlinx.android.synthetic.main.fragment_serie.view.*
import kotlinx.android.synthetic.main.fragment_sinopsis.view.*


class SerieAdapter(val context: Context, val view: View, private val serie: SerieResponse.Serie) {

    fun fillCollapseBar(basic: String?) {
        fillBasics()
        fillImages(basic)
    }

    /**
     * Fills the overview and calls the methods that load genres, networks and trailer
     */
    fun fillOverview() {
        view.seguimiento.visibility = if (serie.followingData.added) View.VISIBLE else View.GONE
        view.sinopsis_text.text = serie.overview.checkNull(context)

        var isTextViewClicked = false
        view.sinopsis_text.setOnClickListener {
            if(isTextViewClicked){
                view.sinopsis_text.maxLines = 4
                isTextViewClicked = false
            } else {
                //This will expand the textview if it is of 2 lines
                view.sinopsis_text.maxLines = Integer.MAX_VALUE
                isTextViewClicked = true
            }
        }

        fillGenres()
        fillNetworks()
        fillTrailer()
    }

    /**
     * Sets the youtube viewer and load the trailer
     */
    private fun fillTrailer() {
        if (serie.video != null) {
            view.trailer_link.setOnClickListener {
                val youtubePlayer =
                    Intent(Intent.ACTION_VIEW, Uri.parse("$BASE_URL_YOUTUBE${serie.video!!.key}"))
                val chooser: Intent =
                    Intent.createChooser(youtubePlayer, context.getString(R.string.open_with));

                if (youtubePlayer.resolveActivity(context.packageManager) != null)
                    context.startActivity(chooser)
            }
        } else view.trailer_link.visibility = View.GONE
    }

    /**
     * Fills the Networks images and sets the click listener
     */
    private fun fillNetworks() {
        val networksList = mutableListOf<SerieResponse.Serie.Network>()
        for (a in 0..2) {
            serie.networks.getOrNull(a)?.let { networksList.add(it) }
        }
        val childCount: Int = view.networks.childCount
        for (i in 0 until childCount) {
            if (i < networksList.size) {
                val button: ImageButton = view.networks.getChildAt(i) as ImageButton
                if (networksList[i].logoPath != null) {
                    button.getImageNoPlaceholder(
                        context,
                        BASE_URL_IMAGES_NETWORK + networksList[i].logoPath.toString()
                    )
                    button.visibility = View.VISIBLE
                    button.setOnClickListener { goToNetworkFragment(i, it) }
                } else button.visibility = View.GONE
            }
        }


//        view.networks.forEachIndexed { index, view ->
//            if (index > networksList.size) {
//                networksList[index].logoPath?.let {
//                    (view as ImageButton).getImageNoPlaceholder(
//                        context, it
//                    )
//                }
//                view.visibility = View.VISIBLE
//                view.setOnClickListener { goToNetworkFragment(index, it) }
//            }
//        }
    }

    /**
     * Fills the Genres buttons and sets the click listener
     */
    private fun fillGenres() {
        val genresList = mutableListOf<SerieResponse.Serie.Genre>()
        for (a in 0..2) {
            serie.genres.getOrNull(a)?.let { genresList.add(it) }
        }
//        for (childView in view.genres.children) {
//            val v = childView as Button
//            //childView is a child of ll
//        }
        val childCount: Int = view.genres.childCount
        for (i in 0 until childCount) {
            if (i < genresList.size) {
                val button: Button = view.genres.getChildAt(i) as Button
                button.text = genresList[i].name
                button.visibility = View.VISIBLE
                button.setOnClickListener { goToGenreFragment(i, it) }
            }
        }
//        view.genres.forEachIndexed { index, view ->
//            if (index > genresList.size) {
//                (view as Button).text = genresList[index].name
//                view.visibility = View.VISIBLE
//                view.setOnClickListener { goToGenreFragment(index, it) }
//            }
//        }
    }


    /**
     * Fills all the images in the fragment
     */
    private fun fillImages(basicPosterPath: String?) {
        if (basicPosterPath == null || basicPosterPath == serie.posterPath) {
            view.posterImage.getImage(context, BASE_URL_IMAGES_POSTER + serie.posterPath.toString())
        } else {
            view.posterImage.getImage(context, BASE_URL_IMAGES_POSTER + basicPosterPath.toString())
        }
        view.imagen_background.getImageNoPlaceholder(
            context, BASE_URL_IMAGES_BACK + serie.backdropPath.toString()
        )
    }

    /**
     * Fill the basic serie info
     */
    private fun fillBasics() {
        view.fechaSerie.text = serie.firstAirDate.changeDateFormat(FORMAT_YEAR)
        view.country_serie.text =
            if (serie.originCountry.isNotEmpty()) serie.originCountry[0] else context.getString(
                R.string.no_data
            )
        if (Util().getLanguageString() == "es-ES") {
            view.status_serie.text = serie.status.translateStatus()
        } else {
            view.status_serie.text = serie.status
        }
//        view.toolbar_layout.title = serie.name
    }


    /**
     * Sets the data to be sent to NetworkFragment and calls it
     *
     * @param pos position of the list
     * @param v   view of the fragment
     */
    private fun goToNetworkFragment(pos: Int, v: View) {
        val bundle = Bundle()
        bundle.putParcelable(ID_NETWORK, serie.networks[pos])
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
        bundle.putParcelable(ID_GENRE, serie.genres[pos])
        Navigation.findNavController(v)
            .navigate(R.id.action_navigation_series_to_genreFragment, bundle)
    }
}

