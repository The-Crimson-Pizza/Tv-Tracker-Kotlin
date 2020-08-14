package com.thecrimsonpizza.tvtrackerkotlin.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TmdbRepository = TmdbRepository
    fun trendMutable() = repository.getTrendingSeries()
    fun newMutable() = repository.getNewSeries()

}
