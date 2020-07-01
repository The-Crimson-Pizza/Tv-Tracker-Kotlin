package com.thecrimsonpizza.tvtrackerkotlin.app.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.login.LoginActivity
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.tutorial.TutorialActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FIRST_OPENED

class SplashScreen : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}