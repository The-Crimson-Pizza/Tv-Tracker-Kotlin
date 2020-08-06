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
import com.github.appintro.SlidePolicy
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.SharedPreferencesController
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.MY_PREFS
import kotlinx.android.synthetic.main.list_series_basic.view.*
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
            mPopulares, R.layout.list_series_basic
        ) {

            itemView.layoutParams.width = (requireView().width * 0.3).toInt()

            val selectedItems = SparseBooleanArray()
            val prefs: SharedPreferences = requireContext().applicationContext.getSharedPreferences(
                MY_PREFS,
                Context.MODE_PRIVATE
            )
            itemView.cardViewBasic.isSelected = selectedItems[adapterPosition, false]
            itemView.posterBasic.getImage(requireContext(), it.posterPath)
            itemView.titleBasic.text = it.name
            itemView.setOnClickListener {
                if (selectedItems[adapterPosition, false]) {
                    selectedItems.delete(adapterPosition)
                    itemView.cardViewBasic.isSelected = false
                    mCodes.remove(adapterPosition)
                } else {
                    selectedItems.put(adapterPosition, true)
                    itemView.cardViewBasic.isSelected = true
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