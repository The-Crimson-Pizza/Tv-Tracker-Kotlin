package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers


class SearchViewModel : ViewModel() {

    private var searchPersonList= MutableLiveData<PersonResponse>()
    private var searchShowList = MutableLiveData<SerieResponse>()
    private var query = MutableLiveData<String>()


    fun setQuery(value: String) {
        query.value = value
    }
    fun getQuery():LiveData<String>{
        return query
    }


    fun setPersonList(query: String) {
        TmdbRepository.searchPerson(query)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(Throwable::printStackTrace)
            .subscribe {
                searchPersonList.value = it
            }
    }
    fun getPersonList(): LiveData<PersonResponse> {
        return searchPersonList
    }


    fun setShowList(query: String) {
        TmdbRepository.searchSerie(query)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(Throwable::printStackTrace)
            .subscribe {
                searchShowList.value = it
            }
    }
    fun getShowList(): LiveData<SerieResponse> {
        return searchShowList
    }
}