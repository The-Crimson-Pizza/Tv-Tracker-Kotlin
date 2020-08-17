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
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Resource

class FollowingViewModel : ViewModel() {

    private val followingMutable = MutableLiveData<Resource<List<SerieResponse.Serie>>>()

    fun getFollowing(): LiveData<Resource<List<SerieResponse.Serie>>> {
        return followingMutable
    }


    fun init() {
        FirebaseDatabaseRealtime(FirebaseAuth.getInstance().currentUser).followingReference.addValueEventListener(
            object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    followingMutable.postValue(Resource.error(data = null, message = "Empty"))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val temp: List<SerieResponse.Serie>? =
                        snapshot.getValue(object :
                            GenericTypeIndicator<List<SerieResponse.Serie>>() {})

                    followingMutable.postValue(Resource.loading(data = null))
                    if (temp != null) followingMutable.postValue(Resource.success(data = temp))
                    else followingMutable.postValue(Resource.error(data = null, message = "Empty"))
                }
            })
    }
}