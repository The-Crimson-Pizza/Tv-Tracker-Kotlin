package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.toLiveData
import io.reactivex.rxjava3.schedulers.Schedulers

class SeriesViewModel : ViewModel() {

    private var serieMutable = MutableLiveData<SerieResponse.Serie>()
//    private var serieMutable = MutableLiveData<StateUi<SerieResponse.Serie>>()

    fun getShowData(id: Int) {
        TmdbRepository.getSerieWithSeasons(id)
            .observeOn(Schedulers.io())
//            .doOnSubscribe(StateUi.Loading)
            .doOnError(Throwable::printStackTrace)
            .subscribe {
                serieMutable.postValue(it)
            }
    }

    fun saveSerie(serie: SerieResponse.Serie) {
        serieMutable.postValue(serie)
    }


    fun getShow(): LiveData<SerieResponse.Serie> {
        return serieMutable
    }
//    fun getShow(): LiveData<StateUi<SerieResponse.Serie>> {
//        return serieMutable
//    }


    fun getFollowingShows(): LiveData<List<SerieResponse.Serie>>? {
        return FollowingViewModel().getFollowing()
    }

    fun getShowsByNetwork(id: Int): LiveData<BasicResponse> {
        return TmdbRepository.getByNetwork(id).toLiveData()
    }

    fun getShowsByGenre(id: Int): LiveData<BasicResponse> {
        return TmdbRepository.getByGenre(id).toLiveData()
    }

}