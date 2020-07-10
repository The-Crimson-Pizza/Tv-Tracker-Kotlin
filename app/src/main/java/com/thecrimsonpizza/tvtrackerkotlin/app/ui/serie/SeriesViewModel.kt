package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.toLiveData

class SeriesViewModel : ViewModel() {

    private lateinit var serieSource: LiveData<SerieResponse.Serie>
    lateinit var serieMutable: MutableLiveData<SerieResponse.Serie>
    private val mediator = MediatorLiveData<Unit>()


    fun getShowData(id: Int): LiveData<SerieResponse.Serie> {
        serieSource = TmdbRepository.getSerieWithSeasons(id).toLiveData()
        mediator.addSource(serieSource) { serieMutable.value = it }
        return serieMutable
    }

    fun saveSerie(serie: SerieResponse.Serie) {
        mediator.addSource(serieSource) { serieMutable.value = serie }
    }


//    fun getShow(): LiveData<SerieResponse.Serie> {
//        return serieMutable    }




    fun getFollowingShows(): MutableLiveData<List<SerieResponse.Serie>>? {
        return FollowingViewModel().followingMutable
    }

    fun getShowsByNetwork(id: Int): LiveData<BasicResponse> {
        return TmdbRepository.getByNetwork(id).toLiveData()
    }

    fun getShowsByGenre(id: Int): LiveData<BasicResponse> {
        return TmdbRepository.getByGenre(id).toLiveData()
    }

}