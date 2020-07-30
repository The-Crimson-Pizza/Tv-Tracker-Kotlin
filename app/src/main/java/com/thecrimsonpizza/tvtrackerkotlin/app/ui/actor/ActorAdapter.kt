package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.webview.WebViewActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_PORTRAIT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_WEB_MOVIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_LONG
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
            BASE_URL_IMAGES_PORTRAIT + person.posterPath,
            view.portraitImage,
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
            if (isTextViewClicked) {
                includeView.bio_text.maxLines = 4
                isTextViewClicked = false
            } else {
                includeView.bio_text.maxLines = Integer.MAX_VALUE
                isTextViewClicked = true
            }
        }

        if (person.movieCredits != null && person.movieCredits.cast.isNotEmpty()) {
            val sortedMovies = person.movieCredits.cast.sortedByDescending { it.releaseDate }
            includeView.rv_movies.setBaseAdapterTwo(
                sortedMovies, R.layout.lista_series_basic
            ) { cast ->
                itemView.titleBasic.text = cast.title
                itemView.posterBasic.getImage(
                    context, BASE_URL_IMAGES_POSTER + cast.posterPath
                )
                itemView.ratingBasic.text = cast.voteAverage.toString()
                itemView.setOnClickListener { v: View -> goToTmdbMovie(cast, context, v) }
            }
        } else includeView.movieCreditsView.visibility = View.GONE

        if (person.tvCredits != null && person.tvCredits.cast.isNotEmpty()) {
            val sortedShows = person.tvCredits.cast.sortedByDescending { it.firstAirDate }
            includeView.rvSeries.setBaseAdapterTwo(
                sortedShows, R.layout.lista_series_basic
            ) { cast ->
                itemView.titleBasic.text = cast.name
                itemView.posterBasic.getImage(
                    context, BASE_URL_IMAGES_POSTER + cast.posterPath
                )
                itemView.ratingBasic.text = cast.voteAverage.toString()
                itemView.setOnClickListener { cast.goToBaseActivity(context, it) }
            }
        } else includeView.tvCreditsView.visibility = View.GONE
    }

    private fun goToTmdbMovie(movie: Credits.Cast, context: Context, view: View) {
        Snackbar.make(
            view,
            context.getString(R.string.go_to_tmdb),
            BaseTransientBottomBar.LENGTH_LONG
        )
            .setAction(R.string.open_web) {
                context.startActivity(
                    Intent(context, WebViewActivity::class.java).putExtra(
                        URL_WEB_VIEW, BASE_URL_WEB_MOVIE + movie.id
                    )
                )
            }.show()
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
