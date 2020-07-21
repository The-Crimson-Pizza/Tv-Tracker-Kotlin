package com.thecrimsonpizza.tvtrackerkotlin.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import io.reactivex.rxjava3.schedulers.Schedulers


class HomeViewModel : ViewModel() {

    private var newMutable = MutableLiveData<BasicResponse>()
    private var trendMutable = MutableLiveData<BasicResponse>()

    fun init() {
        if (trendMutable.value == null) {
            TmdbRepository.getTrendingSeries()
                .observeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .subscribe {
                    trendMutable.postValue(it)
                }
        }
        if (newMutable.value == null) {
            TmdbRepository.getNewSeries()
                .observeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .subscribe {
                    newMutable.postValue(it)
                }
        }
    }

    fun getTrendingShows(): LiveData<BasicResponse> {
        return trendMutable
    }

    fun getNewShows(): LiveData<BasicResponse> {
        return newMutable
    }
}
