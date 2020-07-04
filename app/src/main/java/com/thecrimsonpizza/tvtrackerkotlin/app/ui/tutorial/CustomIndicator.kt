package com.thecrimsonpizza.tvtrackerkotlin.app.ui.tutorial

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.github.appintro.indicator.IndicatorController
import com.thecrimsonpizza.tvtrackerkotlin.R

class CustomIndicator : IndicatorController {

    private val FIRST_PAGE_NUM = 0
    var selectedDotColor = 1
    var unselectedDotColor = 1
    private lateinit var mProgressBar: ProgressBar

    override var unselectedIndicatorColor: Int = 0
        set(value) {
            field = value
            mProgressBar.indeterminateDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    unselectedDotColor, BlendModeCompat.SRC_IN
                )
        }

    override var selectedIndicatorColor: Int = 0
        set(value) {
            field = value
            mProgressBar.indeterminateDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    selectedDotColor, BlendModeCompat.SRC_IN
                )
        }


    override fun initialize(slideCount: Int) {
        mProgressBar.max = slideCount
        selectPosition(FIRST_PAGE_NUM)
    }

    override fun newInstance(context: Context): View {
        mProgressBar =
            View.inflate(context, R.layout.tutorial_progress_bar, null) as ProgressBar
        if (selectedDotColor != selectedIndicatorColor) mProgressBar.progressDrawable.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                selectedDotColor, BlendModeCompat.SRC_IN
            )
        if (unselectedDotColor != selectedIndicatorColor) mProgressBar.indeterminateDrawable.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                unselectedDotColor, BlendModeCompat.SRC_IN
            )

        return mProgressBar
    }

    override fun selectPosition(index: Int) {
        mProgressBar.progress = index + 1
        if (index % 2 == 0) {
            selectedDotColor = selectedIndicatorColor
            mProgressBar.progressDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    selectedIndicatorColor, BlendModeCompat.SRC_IN
                )
        } else {
            selectedDotColor = unselectedIndicatorColor
            mProgressBar.progressDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    unselectedIndicatorColor, BlendModeCompat.SRC_IN
                )
        }
    }
}