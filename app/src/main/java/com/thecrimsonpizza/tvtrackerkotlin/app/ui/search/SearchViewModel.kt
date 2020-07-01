package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.toLiveData


class SearchViewModel : ViewModel() {
    private val query = MutableLiveData<String>()
    private lateinit var searchPersonList: LiveData<PersonResponse>
    private lateinit var searchShowList: LiveData<SerieResponse>

    fun setQuery(value: String) {
        query.value = value
    }

    fun getQuery(): LiveData<String>? {
        return query
    }

    fun initPersonSearch(query: String) {
        searchPersonList = TmdbRepository.searchPerson(query).toLiveData()
    }

    fun getPersonList(): LiveData<PersonResponse> {
        return searchPersonList
    }

    fun initShowSearch(query: String) {
        searchShowList = TmdbRepository.searchSerie(query).toLiveData()
    }

    fun getShowList(): LiveData<SerieResponse> {
        return searchShowList
    }
}