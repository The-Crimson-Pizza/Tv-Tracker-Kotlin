package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImagePortrait
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.goToPersonActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_PORTRAIT
import kotlinx.android.synthetic.main.fragment_cast.*
import kotlinx.android.synthetic.main.lista_cast_vertical.view.*


class CastFragment : Fragment() {

    private val serieViewModel: SeriesViewModel by activityViewModels()
    private val mCast: MutableList<Credits.Cast> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        serieViewModel.getShow().observe(viewLifecycleOwner, Observer { refreshData(it) })
    }

    private fun setAdapter() {
        gridCasting.setBaseAdapter(
            mCast, R.layout.lista_cast_vertical,
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        ) { cast ->
            itemView.actor_name.text = cast.name
            itemView.character_name.text = cast.character
            itemView.profile_image.getImagePortrait(
                requireContext(),
                BASE_URL_IMAGES_PORTRAIT + cast.profilePath.toString()
            )
            itemView.setOnClickListener { v -> cast.goToPersonActivity(requireContext(), v) }
        }
    }

    private fun refreshData(it: SerieResponse.Serie) {
        mCast.clear()
        it.credits.cast.let { it1 -> mCast.addAll(it1) }
        gridCasting.adapter?.notifyDataSetChanged()

        if (mCast.isNotEmpty()) {
            if (R.id.gridCasting == switcher_cast.nextView.id) switcher_cast.showNext()
        } else if (R.id.no_data_cast == switcher_cast.nextView.id) switcher_cast.showNext()
    }

}