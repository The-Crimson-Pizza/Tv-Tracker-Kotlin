package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SerieActivity
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.webview.WebViewActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_PORTRAIT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_WEB_MOVIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_SERIE_POSTER_PATH
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_LONG
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.URL_WEB_VIEW
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.getImageNoPlaceholder
import kotlinx.android.synthetic.main.content_actor.view.*
import kotlinx.android.synthetic.main.fragment_actor.view.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*
import java.time.LocalDate
import java.time.Period

class ActorAdapter(
    val context: Context,
    val view: View,
    private val person: PersonResponse.Person
) {

    fun fillActor() {

        val includeView: View = view.include_actor

        view.toolbar_actor.title = person.name
        getImageNoPlaceholder(
            BASE_URL_IMAGES_PORTRAIT + person.profilePath,
            view.profile_image,
            context
        )

        includeView.fecha_actor.text =
            person.birthday?.let { calculateAge(false) } ?: context.getString(R.string.no_data)

        if (person.isDead) {
            includeView.fecha_actor_mortimer.text = calculateAge(true)
            includeView.fecha_actor_mortimer.visibility = View.VISIBLE
            includeView.icon_mortimer.visibility = View.VISIBLE
        }

        includeView.lugar_actor.text = person.placeOfBirth.checkNull(context)

        includeView.bio_text.text = person.biography.checkNull(context)
        var isTextViewClicked = false
        includeView.bio_text.setOnClickListener {
            if(isTextViewClicked){
                //This will shrink textview to 2 lines if it is expanded.
                includeView.bio_text.maxLines = 2
                isTextViewClicked = false
            } else {
                //This will expand the textview if it is of 2 lines
                includeView.bio_text.maxLines = Integer.MAX_VALUE
                isTextViewClicked = true
            }
        }

        val sortedMovies = person.movieCredits.cast.sortedByDescending { it.releaseDate }
        person.movieCredits.cast.let {
            includeView.rv_movies.setBaseAdapter(
                sortedMovies, R.layout.lista_series_basic,
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            ) { cast ->
                itemView.titleBasic.text = cast.title
                itemView.posterBasic.getImage(
                    context, BASE_URL_IMAGES_POSTER + cast.posterPath
                )
                itemView.ratingBasic.text = cast.voteAverage.toString()
                itemView.setOnClickListener { v: View -> goToTmdbMovie(cast, context, v) }
            }
        }

        val sortedShows = person.tvCredits.cast.sortedByDescending { it.firstAirDate }
        person.tvCredits.cast.let {
            includeView.rvSeries.setBaseAdapter(
                sortedShows, R.layout.lista_series_basic,
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            ) { cast ->
                itemView.titleBasic.text = cast.name
                itemView.posterBasic.getImage(
                    context, BASE_URL_IMAGES_POSTER + cast.posterPath
                )
                itemView.ratingBasic.text = cast.voteAverage.toString()
                itemView.setOnClickListener { v: View -> goToSerie(cast, v) }
            }
        }

        if (person.movieCredits.cast.isNullOrEmpty())
            includeView.movieCreditsView.visibility = View.GONE
        else includeView.movieCreditsView.visibility = View.VISIBLE

        if (person.tvCredits.cast.isNullOrEmpty()) includeView.tvCreditsView.visibility = View.GONE
        else includeView.tvCreditsView.visibility = View.VISIBLE

    }

    private fun goToTmdbMovie(movie: Credits.Cast, context: Context, view: View) {
        Snackbar.make(view, "Ver ficha técnica", BaseTransientBottomBar.LENGTH_LONG)
            .setAction(R.string.open_web) {
                context.startActivity(
                    Intent(context, WebViewActivity::class.java).putExtra(
                        URL_WEB_VIEW, BASE_URL_WEB_MOVIE + movie.id
                    )
                )
            }.show()
    }

    private fun goToSerie(cast: Credits.Cast, view: View) {
        val intent = Intent(context, SerieActivity::class.java).apply {
            putExtras(Bundle().apply {
                putExtra(ID_SERIE, cast.id)
                putExtra(BASIC_SERIE_POSTER_PATH, cast.posterPath)
            })
        }
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            context as Activity, Pair(view.posterBasic, view.posterBasic.transitionName)
        )
        ActivityCompat.startActivity(context, intent, activityOptions.toBundle())
    }

    private fun calculateAge(dead: Boolean): String {
        val bornDateNew: String
        var deathDateNew = ""

        if (person.isDead) {
            val birth: LocalDate = person.birthday.parseToLocalDate()
            val death: LocalDate = person.deathday.parseToLocalDate()
            val year: Int = Period.between(birth, death).years
            bornDateNew =
                String.format("%s", person.birthday?.changeDateFormat(FORMAT_LONG))
            deathDateNew = String.format(
                "%s (%s %s)",
                person.deathday?.changeDateFormat(FORMAT_LONG),
                year,
                context.getString(R.string.years)
            )
        } else {
            val date: LocalDate = person.birthday.parseToLocalDate()
            val year: Int = Period.between(date, LocalDate.now()).years
            bornDateNew = String.format(
                "%s (%s %s)",
                person.birthday?.changeDateFormat(FORMAT_LONG),
                year, context.getString(R.string.years)
            )
        }

        return if (dead) deathDateNew else bornDateNew

    }
}
