package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_PERSON
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

        val person = intent.getParcelableExtra<Credits.Cast>(BASIC_PERSON)

        personViewModel.retrievePerson(idPerson)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ActorFragment(person)).commit()
    }

}