package com.thecrimsonpizza.tvtrackerkotlin.app.ui.tutorial

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.appintro.SlidePolicy
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.SharedPreferencesController
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.MY_PREFS
import kotlinx.android.synthetic.main.lista_series_basic_vertical_tutorial.*
import kotlinx.android.synthetic.main.tutorial_slide5.*

class IntroductionFragment : Fragment(), SlidePolicy {

    private val tutorialViewModel: TutorialViewModel by activityViewModels()
    private val mPopulares = mutableListOf<BasicResponse.SerieBasic>()
    private val mCodes = HashMap<Int, Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tutorial_slide5, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tutorialViewModel.getTrendingShows().observe(viewLifecycleOwner, Observer {
            mPopulares.clear()
            mPopulares.addAll(it.basicSeries)
            recyclerTutorial.adapter?.notifyDataSetChanged()
        })
        setAdapter()
    }

    private fun setAdapter() {
        recyclerTutorial.setBaseAdapter(
            mPopulares, R.layout.lista_series_basic_vertical_tutorial,
            GridLayoutManager(activity, 3)
        ) {
            val selectedItems = SparseBooleanArray()
            val prefs: SharedPreferences = requireContext().applicationContext.getSharedPreferences(
                MY_PREFS,
                Context.MODE_PRIVATE
            )
            cardViewTutorial.isSelected = selectedItems[adapterPosition, false]
            posterBasicTutorial.getImage(requireContext(), it.posterPath)
            titleBasicTutorial.text = it.name
            itemView.setOnClickListener {
                if (selectedItems[adapterPosition, false]) {
                    selectedItems.delete(adapterPosition)
                    cardViewTutorial.isSelected = false
                    mCodes.remove(adapterPosition)
                } else {
                    selectedItems.put(adapterPosition, true)
                    cardViewTutorial.isSelected = true
                    mCodes[adapterPosition] = id
                }
                SharedPreferencesController().setPrefIntArray(
                    prefs,
                    ArrayList<Int>(mCodes.values)
                        .stream().mapToInt { i: Int? -> i!! }.toArray()
                )
            }
        }
    }

    override val isPolicyRespected: Boolean
        get() = true

    override fun onUserIllegallyRequestedNextPage() {
        TODO("Not yet implemented")
    }
}