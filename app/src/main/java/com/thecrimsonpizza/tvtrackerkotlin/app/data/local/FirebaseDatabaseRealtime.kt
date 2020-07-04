package com.thecrimsonpizza.tvtrackerkotlin.app.data.local

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse

/**
 * Class that manages the connections to the Firebase Database, getting and uploading data
 */
class FirebaseDatabaseRealtime(currentUser: FirebaseUser?) {

    val seriesFav: DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("users/" + currentUser?.uid + "/series_following")

    private val tempIds: DatabaseReference = FirebaseDatabase.getInstance().getReference("temp/")

    fun setSeriesFav(followingList: List<SerieResponse.Serie>) {
        seriesFav.setValue(followingList)
    }
}