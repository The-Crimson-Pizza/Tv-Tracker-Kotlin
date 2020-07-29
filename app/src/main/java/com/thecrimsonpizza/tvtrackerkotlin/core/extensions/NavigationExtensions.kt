package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_SERIE_POSTER_PATH
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TYPE_FRAGMENT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Type
import kotlinx.android.synthetic.main.lista_cast_vertical.view.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*

fun BasicResponse.SerieBasic.goToBaseActivity(context: Context, view: View) {
    val serie = this
    val intent = Intent(context, BaseActivity::class.java).apply {
        putExtras(Bundle().apply {
            putExtra(TYPE_FRAGMENT, Type.SERIE)
            putExtra(ID_SERIE, serie.id)
            putExtra(BASIC_SERIE_POSTER_PATH, serie.posterPath)
        })
    }

    val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
        context as Activity,
        Pair(view.posterBasic, view.posterBasic.transitionName)
    )

    ActivityCompat.startActivity(context, intent, activityOptions.toBundle())
}

fun Credits.Cast.goToPersonActivity(context: Context, v: View) {
    val cast: Credits.Cast = this
    val intent = Intent(context, BaseActivity::class.java).apply {
        putExtras(Bundle().apply {
            putExtra(TYPE_FRAGMENT, Type.PERSON)
            putExtra(GlobalConstants.ID_ACTOR, cast.id)
            putParcelable(GlobalConstants.BASIC_PERSON_POSTER_PATH, cast)
        })
    }
    val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
        context as Activity, Pair(v.profile_image, v.profile_image.transitionName)
    )

    ActivityCompat.startActivity(context, intent, activityOptions.toBundle())
}

