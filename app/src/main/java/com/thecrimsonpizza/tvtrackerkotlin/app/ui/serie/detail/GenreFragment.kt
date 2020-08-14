package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.goToBaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import kotlinx.android.synthetic.main.fragment_genre_network.*
import kotlinx.android.synthetic.main.list_series_basic.view.*

class GenreFragment(genre: BaseClass?) : Fragment() {

    private val seriesViewModel: SeriesViewModel by activityViewModels()

    private var mGenre = genre as SerieResponse.Serie.Genre
    private var page = 1
    private val mSeriesByGenre: MutableList<BasicResponse.SerieBasic> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_genre_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seriesViewModel.retrieveShowsByGenre(mGenre.id, page++)
        setGenreIcon()
        getSeriesByGenre()
        setAdapter()

        rv_genres.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = rv_genres.layoutManager as GridLayoutManager

//                lastVisibleItemId = rv_genres.layoutManager.findLastVisibleItemPosition()
                if (layoutManager.findLastCompletelyVisibleItemPosition() == mSeriesByGenre.size - 1) {
                    seriesViewModel.retrieveShowsByGenre(mGenre.id, page++)
                }
            }
        })
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
        seriesViewModel.getShowsByGenre()
            .observe(viewLifecycleOwner, Observer<BasicResponse> {
//                mSeriesByGenre.clear()
                mSeriesByGenre.addAll(it.basicSeries)
                rv_genres.adapter?.notifyItemRangeChanged(
                    mSeriesByGenre.size + 1,
                    it.basicSeries.size
                );
                rv_genres.adapter?.notifyDataSetChanged()
                if (page == 2) rv_genres.scheduleLayoutAnimation()
            })
    }


    private fun setAdapter() {
        rv_genres.setBaseAdapter(
            mSeriesByGenre,
            R.layout.list_series_basic
        ) { serie ->
            itemView.layoutParams.width = (requireView().width * 0.3).toInt()

            itemView.posterBasic.getImage(
                requireContext(), GlobalConstants.BASE_URL_IMAGES_POSTER + serie.posterPath
            )
            itemView.titleBasic.text = serie.name
            itemView.ratingBasic.text = serie.voteAverage.toString()
            itemView.ratingBasic.visibility = View.GONE

            itemView.setOnClickListener { serie.goToBaseActivity(requireContext(), it) }

            setAnimation(itemView, adapterPosition);

        }


    }

    private var lastPosition = -1
    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_up)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}