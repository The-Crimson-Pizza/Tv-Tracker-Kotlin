package com.thecrimsonpizza.tvtrackerkotlin.app.ui.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.login.LoginActivity
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.tutorial.TutorialActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FIRST_OPENED

class SplashScreen : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.splashscreen_layout)

        val firstOpened =
            getPreferences(Context.MODE_PRIVATE).getBoolean(FIRST_OPENED, true)
        if (firstOpened) {
            getPreferences(Context.MODE_PRIVATE).edit()
                .putBoolean(FIRST_OPENED, false).apply()
            startActivity(Intent(this, TutorialActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation(this)
        }
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}