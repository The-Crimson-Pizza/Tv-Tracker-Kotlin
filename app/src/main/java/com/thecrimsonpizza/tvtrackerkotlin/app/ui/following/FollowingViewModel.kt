package com.thecrimsonpizza.tvtrackerkotlin.app.ui.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.FirebaseDatabaseRealtime
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse

class FollowingViewModel : ViewModel() {

    private val followingMutable = MutableLiveData<List<SerieResponse.Serie>>()

    fun getFollowing(): LiveData<List<SerieResponse.Serie>>{
        return followingMutable
    }

    fun init() {
        FirebaseDatabaseRealtime(FirebaseAuth.getInstance().currentUser).seriesFav.addValueEventListener(
            object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    followingMutable.value = null
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val temp: List<SerieResponse.Serie>? =
                        snapshot.getValue(object :
                            GenericTypeIndicator<List<SerieResponse.Serie>>() {})

                    if (temp != null) followingMutable.value = temp
                }
            })
    }
}