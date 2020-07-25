package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_SERIE_POSTER_PATH
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE

class SerieActivity : AppCompatActivity() {

    private val seriesViewModel: SeriesViewModel by lazy {
        ViewModelProvider(this@SerieActivity).get(SeriesViewModel::class.java)
    }
    private var idSerie: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_detail)

        idSerie = intent.getIntExtra(ID_SERIE, 0)

        val basicPosterPath = intent.getStringExtra(BASIC_SERIE_POSTER_PATH)

        seriesViewModel.getShowData(idSerie)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SerieFragment(basicPosterPath))
            .commit()
    }
}
