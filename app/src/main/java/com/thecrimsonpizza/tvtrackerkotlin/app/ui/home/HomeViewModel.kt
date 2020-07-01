package com.thecrimsonpizza.tvtrackerkotlin.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse.Serie
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.toLiveData


class HomeViewModel : ViewModel() {

    fun getTrendingShows(): LiveData<BasicResponse> {
        return TmdbRepository.getTrendingSeries().toLiveData()
    }

    fun getNewShows(): LiveData<BasicResponse> {
        return TmdbRepository.getNewSeries().toLiveData()
    }

    fun getFollowingShows(): MutableLiveData<List<Serie>>? {
        return FollowingViewModel().followingMutable
    }
}
