package com.thecrimsonpizza.tvtrackerkotlin.core.base

sealed class StateUi {
    data class Success(val data: Any) : StateUi()
    object Loading : StateUi()
    class Error(val throwable: Throwable) : StateUi()
}