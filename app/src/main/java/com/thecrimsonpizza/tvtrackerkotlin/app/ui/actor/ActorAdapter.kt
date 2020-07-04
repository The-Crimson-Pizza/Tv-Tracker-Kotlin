package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.MovieCredits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.TvCredits
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.webview.WebViewActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.*
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_PORTRAIT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_WEB_MOVIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FORMAT_LONG
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.URL_WEBVIEW
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.getImageNoPlaceholder
import kotlinx.android.synthetic.main.content_actor.view.*
import kotlinx.android.synthetic.main.fragment_actor.view.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*
import java.time.LocalDate
import java.time.Period

class ActorAdapter(val context: Context, val view: View, val person: PersonResponse.Person) {

    /**
     * Fills the data obtained by the api in the ActorFragment
     */
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

        person.movieCredits?.cast?.let {
            includeView.rv_movies.setMovieCreditsAdapter(
                it, R.layout.lista_series_basic,
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            ) { cast ->
                itemView.titleBasicTutorial.text = cast.title
                itemView.posterBasic.getImage(
                    context, BASE_URL_IMAGES_POSTER + cast.posterPath
                )
                itemView.ratingBasic.text = cast.voteAverage.toString()
                itemView.setOnClickListener { v: View -> goToTmdbMovie(cast, context, v) }
            }
        }

        person.tvCredits?.cast?.let {
            includeView.rvSeries.setTvCreditsAdapter(
                it, R.layout.lista_series_basic,
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            ) { cast ->
                itemView.titleBasicTutorial.text = cast.name
                itemView.posterBasic.getImage(
                    context, BASE_URL_IMAGES_POSTER + cast.posterPath
                )
                itemView.ratingBasic.text = cast.voteAverage.toString()
                itemView.setOnClickListener { v: View -> goToSerie(cast, v) }
            }


        }

        if (person.movieCredits?.cast.isNullOrEmpty())
            includeView.movieCreditsView.visibility = View.GONE
        else includeView.movieCreditsView.visibility = View.VISIBLE

        if (person.tvCredits?.cast.isNullOrEmpty()) includeView.tvCreditsView.visibility = View.GONE
        else includeView.tvCreditsView.visibility = View.VISIBLE

    }

    private fun goToTmdbMovie(movie: MovieCredits.Cast, context: Context, view: View) {
        Snackbar.make(view, "Ver ficha técnica", BaseTransientBottomBar.LENGTH_LONG)
            .setAction(R.string.open_web) {
                context.startActivity(
                    Intent(context, WebViewActivity::class.java).putExtra(
                        URL_WEBVIEW, BASE_URL_WEB_MOVIE + movie.id
                    )
                )
            }.show()
    }

    private fun goToSerie(serie: TvCredits.Cast, view: View) {
        val bundle = Bundle()
        bundle.putInt(ID_SERIE, serie.id)
        Navigation.findNavController(view).navigate(R.id.action_actores_to_series, bundle)
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

        return if (dead) {
            deathDateNew
        } else {
            bornDateNew
        }

    }
}
