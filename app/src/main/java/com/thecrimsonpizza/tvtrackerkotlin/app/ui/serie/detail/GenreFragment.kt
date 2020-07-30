package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.goToBaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapterTwo
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import kotlinx.android.synthetic.main.fragment_genre_network.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*

class GenreFragment(genre: BaseClass?) : Fragment() {

    private val seriesViewModel: SeriesViewModel by activityViewModels()

    private var mGenre = genre as SerieResponse.Serie.Genre
    private val mSeriesByGenre: MutableList<BasicResponse.SerieBasic> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_genre_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGenreIcon()
        getSeriesByGenre()
        setAdapter()
    }

    private fun setGenreIcon() {
        val genreName: String = mGenre.name
        genre_name.text = genreName
        when (mGenre.id) {
            10759 -> genre_icon.setImageResource(R.drawable.genre_adventure)
            16 -> genre_icon.setImageResource(R.drawable.genre_animation)
            35 -> genre_icon.setImageResource(R.drawable.genre_comedy)
            99 -> genre_icon.setImageResource(R.drawable.genre_camera)
            18 -> genre_icon.setImageResource(R.drawable.genre_drama)
            10751 -> genre_icon.setImageResource(R.drawable.genre_family)
            10762 -> genre_icon.setImageResource(R.drawable.genre_kids)
            9648 -> genre_icon.setImageResource(R.drawable.genre_mistery)
            80 -> genre_icon.setImageResource(R.drawable.genre_crime)
            10763 -> genre_icon.setImageResource(R.drawable.genre_news)
            10764 -> genre_icon.setImageResource(R.drawable.genre_reality)
            10765 -> genre_icon.setImageResource(R.drawable.genre_sci_fi)
            10766 -> genre_icon.setImageResource(R.drawable.genre_soap)
            10767 -> genre_icon.setImageResource(R.drawable.genre_talk)
            10768 -> genre_icon.setImageResource(R.drawable.genre_war)
            37 -> genre_icon.setImageResource(R.drawable.genre_western)
            else -> genre_icon.setImageResource(R.drawable.genre_documental)
        }
    }

    private fun getSeriesByGenre() {
        seriesViewModel.getShowsByGenre(mGenre.id)
            .observe(viewLifecycleOwner, Observer<BasicResponse> {
                mSeriesByGenre.clear()
                mSeriesByGenre.addAll(it.basicSeries)
                rv_genres.adapter?.notifyDataSetChanged()
            })
    }

    private fun setAdapter() {
        rv_genres.setBaseAdapterTwo(
            mSeriesByGenre,
            R.layout.lista_series_basic
        ) { serie ->
            itemView.posterBasic.getImage(
                requireContext(), GlobalConstants.BASE_URL_IMAGES_POSTER + serie.posterPath
            )
            itemView.titleBasic.text = serie.name
            itemView.ratingBasic.text = serie.voteAverage.toString()
            itemView.ratingBasic.visibility = View.GONE

            itemView.setOnClickListener { serie.goToBaseActivity(requireContext(), it) }
        }
    }
}