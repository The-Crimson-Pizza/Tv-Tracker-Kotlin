package com.thecrimsonpizza.tvtrackerkotlin.app.ui.tutorial

import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository


class TutorialViewModel : ViewModel() {

    fun trendMutable() = TmdbRepository.getTrendingSeries()
}
