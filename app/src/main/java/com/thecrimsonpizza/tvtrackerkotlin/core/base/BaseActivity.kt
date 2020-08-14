package com.thecrimsonpizza.tvtrackerkotlin.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor.ActorFragment
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor.ActorViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SerieFragment
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.detail.GenreFragment
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.detail.NetworkFragment
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_SERIE_POSTER_PATH
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.DATA
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TYPE_FRAGMENT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Type

class BaseActivity : AppCompatActivity() {

    private val seriesViewModel: SeriesViewModel by lazy {
        ViewModelProvider(this@BaseActivity).get(SeriesViewModel::class.java)
    }
    private var idSerie: Int = 0

    private val personViewModel: ActorViewModel by lazy {
        ViewModelProvider(this@BaseActivity).get(ActorViewModel::class.java)
    }
    private var idPerson: Int = 0
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity_layout)

        id = intent.getIntExtra(ID, 0)

        when (intent.getSerializableExtra(TYPE_FRAGMENT)) {
            Type.SERIE -> {
                idSerie = intent.getIntExtra(ID_SERIE, 0)
                val basicPosterPath = intent.getStringExtra(BASIC_SERIE_POSTER_PATH)
                seriesViewModel.getShowData(idSerie)
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, SerieFragment(idSerie, basicPosterPath)).commit()
            }

            Type.PERSON, Type.CAST -> {
                idPerson = intent.getIntExtra(GlobalConstants.ID_ACTOR, 0)
                val personPath = intent.getStringExtra(GlobalConstants.BASIC_PERSON_POSTER_PATH)
                personViewModel.retrievePerson(idPerson)
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, ActorFragment(personPath)).commit()
            }

            Type.GENRE -> {
                val data = intent.getParcelableExtra<BaseClass>(DATA)
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, GenreFragment(data)).commit()
            }

            Type.NETWORK -> {
                val data = intent.getParcelableExtra<BaseClass>(DATA)
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, NetworkFragment(data)).commit()
            }
        }
    }
}
