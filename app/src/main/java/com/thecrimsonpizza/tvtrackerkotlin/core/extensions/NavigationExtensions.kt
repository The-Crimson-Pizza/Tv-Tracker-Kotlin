package com.thecrimsonpizza.tvtrackerkotlin.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseClass
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_PERSON_POSTER_PATH
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_SERIE_POSTER_PATH
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.DATA
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_ACTOR
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TYPE_FRAGMENT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Type
import kotlinx.android.synthetic.main.list_series_basic.view.*
import kotlinx.android.synthetic.main.lista_cast_vertical.view.*

fun BaseClass.goToBaseActivity(context: Context, view: View) {
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

fun BaseClass.goToBaseActivityNoAnimation(context: Context, type: Type) {
    val serie = this
    val intent = Intent(context, BaseActivity::class.java).apply {
        putExtras(Bundle().apply {
            putExtra(TYPE_FRAGMENT, type)
            putParcelable(DATA, serie)
//            putExtra(BASIC_SERIE_POSTER_PATH, serie.posterPath)
        })
    }
    ActivityCompat.startActivity(context, intent, null)
}

fun BaseClass.goToPersonActivity(context: Context, v: View, type:Type) {
    val cast = this
    val intent = Intent(context, BaseActivity::class.java).apply {
        putExtras(Bundle().apply {
            putExtra(TYPE_FRAGMENT, type)
            putExtra(ID_ACTOR, cast.id)
            putParcelable(BASIC_PERSON_POSTER_PATH, cast)
        })
    }

    val pair = if (cast.javaClass.simpleName == "Cast") {
        Pair.create<View, String>(v.profile_image, v.profile_image.transitionName)
    } else {
        Pair.create<View, String>(v.posterBasic, v.posterBasic.transitionName)
    }

    val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
        context as Activity, pair
    )

    ActivityCompat.startActivity(context, intent, activityOptions.toBundle())
}

