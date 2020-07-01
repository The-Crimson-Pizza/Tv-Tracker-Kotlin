package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.toLiveData


class ActorViewModel : ViewModel() {

    fun getActor(id: Int): LiveData<PersonResponse.Person>? {
        return TmdbRepository.getPerson(id).toLiveData()
    }
}
