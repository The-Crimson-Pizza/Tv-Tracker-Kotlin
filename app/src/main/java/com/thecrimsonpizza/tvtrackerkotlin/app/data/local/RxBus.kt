package com.thecrimsonpizza.tvtrackerkotlin.app.data.local

import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Class that sends the Serie data from [@SerieFragment] to [@SinopsisFragment]
 */
class RxBus private constructor() {
    private val publisher: PublishSubject<SerieResponse.Serie> =
        PublishSubject.create<SerieResponse.Serie>()

    fun publish(event: SerieResponse.Serie) {
        publisher.onNext(event)
    }

    fun listen(): Observable<SerieResponse.Serie> {
        return publisher
    }

    companion object {
        private var mInstance: RxBus? = null
        val instance: RxBus?
            get() {
                if (mInstance == null) {
                    mInstance = RxBus()
                }
                return mInstance
            }
    }
}