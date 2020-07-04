package com.thecrimsonpizza.tvtrackerkotlin.app.ui.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.toLiveData


class TutorialViewModel : ViewModel() {

    fun getTrendingShows(): LiveData<BasicResponse> {
        return TmdbRepository.getTrendingSeries().toLiveData()
    }
}
