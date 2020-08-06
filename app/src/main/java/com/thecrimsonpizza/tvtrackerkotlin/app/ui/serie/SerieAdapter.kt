package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat

import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_BACK
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_NETWORK
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_YOUTUBE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.DATA
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_YEAR
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TYPE_FRAGMENT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Type
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Util
import kotlinx.android.synthetic.main.fragment_serie.view.*
import kotlinx.android.synthetic.main.fragment_serie_details.view.*
import kotlinx.android.synthetic.main.list_genres.view.*
import kotlinx.android.synthetic.main.list_networks.view.*


class SerieAdapter(val context: Context, val view: View, private val serie: SerieResponse.Serie) {

    fun fillCollapseBar(basic: String?) {
        fillBasics()
        fillImages(basic)
    }


    fun fillOverview() {
        view.seguimiento.visibility = if (serie.followingData.added) View.VISIBLE else View.GONE
        view.sinopsis_text.text = serie.overview.checkNull(context)

        var isTextViewClicked = false
        view.sinopsis_text.setOnClickListener {
            if (isTextViewClicked) {
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


    private fun fillTrailer() {
        if (serie.video != null) {
            view.trailer_link.setOnClickListener {
                val youtubePlayer =
                    Intent(Intent.ACTION_VIEW, Uri.parse("$BASE_URL_YOUTUBE${serie.video!!.key}"))
                val chooser: Intent =
                    Intent.createChooser(youtubePlayer, context.getString(R.string.open_with))

                if (youtubePlayer.resolveActivity(context.packageManager) != null)
                    context.startActivity(chooser)
            }
        } else view.trailer_link.visibility = View.GONE
    }


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


    private fun fillBasics() {
        view.fechaSerie.text =
            serie.firstAirDate?.changeDateFormat(FORMAT_YEAR) ?: context.getString(R.string.no_data)
        view.country_serie.text =
            if (serie.originCountry.isNotEmpty()) serie.originCountry[0] else context.getString(
                R.string.no_data
            )
        if (Util().getLanguageString() == "es-ES") {
            view.status_serie.text = serie.status.translateStatus()
        } else {
            view.status_serie.text = serie.status
        }
        view.toolbar_layout.title = serie.name
    }


    private fun fillNetworks() {
        if (!serie.networks.isNullOrEmpty()) {
            view.recyclerNetworks.setBaseAdapter(
                serie.networks, R.layout.list_networks
            ) { network ->
                itemView.ibNetwork.getImageNoPlaceholder(
                    context, BASE_URL_IMAGES_NETWORK + network.posterPath.toString()
                )
                itemView.ibNetwork.setOnClickListener {
                    network.goToBaseActivityNoAnimation(
                        context,
                        Type.NETWORK
                    )
                }
            }
        } else view.networks.visibility = View.GONE
    }


    private fun fillGenres() {
        if (!serie.genres.isNullOrEmpty()) {
            view.recyclerGenres.setBaseAdapter(serie.genres, R.layout.list_genres) { genre ->
                itemView.btGenre.text = genre.name
                itemView.btGenre.setOnClickListener {
                    genre.goToBaseActivityNoAnimation(context, Type.GENRE)
                }
            }
        } else view.genres.visibility = View.GONE
    }

    private fun <T : BaseClass> goToBaseActivity(type: Type, data: T) {
        val intent = Intent(context, BaseActivity::class.java).apply {
            putExtras(Bundle().apply {
                putExtra(TYPE_FRAGMENT, type)
                putParcelable(DATA, data)
//                putExtra(GlobalConstants.BASIC_SERIE_POSTER_PATH, serie.posterPath)
            })
        }
        ActivityCompat.startActivity(context, intent, null)
    }
}

