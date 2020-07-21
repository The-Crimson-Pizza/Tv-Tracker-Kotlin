package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.data.remote.TmdbRepository
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import io.reactivex.rxjava3.schedulers.Schedulers


class ActorViewModel : ViewModel() {

    private var personMutable = MutableLiveData<PersonResponse.Person>()

    fun retrievePerson(id: Int) {
        TmdbRepository.getPerson(id)
            .observeOn(Schedulers.io())
            .doOnError(Throwable::printStackTrace)
            .subscribe {
                personMutable.postValue(it)
            }
    }

    fun getPerson(): LiveData<PersonResponse.Person> {
        return personMutable
    }
}
