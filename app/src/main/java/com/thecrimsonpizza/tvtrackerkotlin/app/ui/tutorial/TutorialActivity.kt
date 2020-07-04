package com.thecrimsonpizza.tvtrackerkotlin.app.ui.tutorial

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroCustomLayoutFragment
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.login.LoginActivity

class TutorialActivity: AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.tutorial_slide1))
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.tutorial_slide2))
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.tutorial_slide3))
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.tutorial_slide4))
        addSlide(IntroductionFragment())

        setImmersiveMode()
        isSystemBackButtonLocked = true
        setProgressIndicator()
        setIndicatorColor(getColor(R.color.bgTotal), getColor(R.color.colorPrimary))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}