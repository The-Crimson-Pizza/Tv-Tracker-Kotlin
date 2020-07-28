package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_GENRE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import kotlinx.android.synthetic.main.fragment_genre.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*

class GenreFragment : Fragment() {

    private val seriesViewModel: SeriesViewModel by activityViewModels()

    private lateinit var mGenre: SerieResponse.Serie.Genre
    private val mSeriesByGenre: MutableList<BasicResponse.SerieBasic> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGenre = requireArguments().getParcelable(ID_GENRE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_genre, container, false)
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
        rv_genres.setBaseAdapter(
            mSeriesByGenre,
            R.layout.lista_series_basic,
            GridLayoutManager(activity, 3)
        ) { serie ->
            itemView.posterBasic.getImage(requireContext(), GlobalConstants.BASE_URL_IMAGES_POSTER +serie.posterPath)
            itemView.titleBasic.text = serie.name
            itemView.ratingBasic.text = serie.voteAverage.toString()

            itemView.ratingBasic.visibility = View.GONE
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(ID_SERIE, serie.id)
                Navigation.findNavController(it)
                    .navigate(R.id.action_global_navigation_series, bundle)
            }
        }
    }
}