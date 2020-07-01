package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.MovieCredits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.TvCredits
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import kotlinx.android.synthetic.main.lista_series_basic.view.*

/**
 * Adapter for the RecyclerView that hosts the info of an actor's movies and shows
 */
class PeopleCreditsAdapter(
    val context: Context,
    var shows: List<TvCredits.Cast>? = null,
    var movies: List<MovieCredits.Cast>? = null
) : RecyclerView.Adapter<PeopleCreditsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.lista_series_basic, parent, false)
        )

    override fun getItemCount(): Int {
        if (movies != null) {
            return movies!!.size
        }
        if (shows != null) {
            return shows!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: PeopleCreditsAdapter.ViewHolder, position: Int) {
        if (movies != null) holder.bind(movies!![position], context)
        else holder.bind(shows!![position], context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cast: MovieCredits.Cast, context: Context) = with(itemView) {
            itemView.titleBasic.text = cast.title
            itemView.posterBasicVertical.getImage(context, BASE_URL_IMAGES_POSTER + cast.posterPath)
            itemView.ratingBasic.text = cast.voteAverage.toString()
        }

        fun bind(cast: TvCredits.Cast, context: Context) = with(itemView) {
            itemView.titleBasic.text = cast.name
            itemView.posterBasicVertical.getImage(context, BASE_URL_IMAGES_POSTER + cast.posterPath)
            itemView.ratingBasic.text = cast.voteAverage.toString()
        }
    }
}


