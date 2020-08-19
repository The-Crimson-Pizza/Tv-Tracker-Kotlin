package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.toLiveData
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Resource
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SeriesViewModel : ViewModel() {

    private val repository: TmdbRepository = TmdbRepository
    private var serieMutable = MutableLiveData<Resource<SerieResponse.Serie>>()
    private var followingData = MutableLiveData<Resource<SerieResponse.Serie>>()
    private var genres = MutableLiveData<BasicResponse>()

    fun getShowData(id: Int) = viewModelScope.launch(IO) {
        serieMutable.postValue(Resource.loading(data = null))
        try {
            serieMutable.postValue(Resource.success(data = repository.getSerie(id)))
        } catch (e: Exception) {
            serieMutable.postValue(
                Resource.error(
                    data = null,
                    message = e.localizedMessage ?: "Fatal Error"
                )
            )
        }
    }

    fun saveSerie(serie: SerieResponse.Serie) {
        val temp = serieMutable.value
        temp?.data = serie
        serieMutable.postValue(temp)
    }

    fun getShow(): LiveData<Resource<SerieResponse.Serie>> {
        return serieMutable
    }

    fun getFollowingShows(): LiveData<Resource<List<SerieResponse.Serie>>>? {
        return FollowingViewModel().getFollowing()
    }

    fun getShowsByNetwork(id: Int): LiveData<BasicResponse> {
        return TmdbRepository.getByNetwork(id).toLiveData()
    }

    fun getShowsByGenre(): LiveData<BasicResponse> {
        return genres
    }

    fun retrieveShowsByGenre(id: Int, page: Int = 1) {
        TmdbRepository.getByGenre(id, page)
            .observeOn(Schedulers.io())
            .doOnError(Throwable::printStackTrace)
            .subscribe {
                genres.postValue(it)
            }
    }
}