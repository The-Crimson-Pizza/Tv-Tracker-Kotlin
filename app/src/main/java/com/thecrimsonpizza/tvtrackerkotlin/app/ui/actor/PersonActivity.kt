package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_PERSON_POSTER_PATH
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_ACTOR

class PersonActivity : AppCompatActivity() {

    private val personViewModel: ActorViewModel by lazy {
        ViewModelProvider(this@PersonActivity).get(ActorViewModel::class.java)
    }
    private var idPerson: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_detail)

        idPerson = intent.getIntExtra(ID_ACTOR, 0)

        val personPath = intent.getStringExtra(BASIC_PERSON_POSTER_PATH)

        personViewModel.retrievePerson(idPerson)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ActorFragment(personPath)).commit()
    }

}
