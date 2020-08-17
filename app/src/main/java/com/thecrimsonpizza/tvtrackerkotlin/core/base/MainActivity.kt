package com.thecrimsonpizza.tvtrackerkotlin.core.base

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.seasons.Season
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse.Serie
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.SEASON_ID_EXTRA
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.SEASON_NUMBER_EXTRA
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.SERIE_NAME_EXTRA
import com.thecrimsonpizza.tvtrackerkotlin.device.Receiver
import com.thecrimsonpizza.tvtrackerkotlin.device.Receiver.Companion.ACTION_ALARM_RECEIVER
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private val followingViewModel: FollowingViewModel by lazy {
        ViewModelProvider(this@MainActivity).get(FollowingViewModel::class.java)
    }

    private lateinit var navController: NavController
    private var startPos = 0
    private var newPos = 0
    private val mFavs: MutableList<Serie> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        followingViewModel.init()
        navController = Navigation.findNavController(
            this@MainActivity,
            R.id.nav_host_fragment
        )
        NavigationUI.setupWithNavController(bottom_nav_view, navController)
        startPos = 0
        setNavigationView()

//        getFavorites()

    }


    private fun getFavorites() {
        followingViewModel.getFollowing().observe(this, Observer {
            mFavs.clear()
            it.data?.let { temp -> mFavs.addAll(temp) }
            for (serie in mFavs) {
                if (serie.seasons.isNotEmpty() && serie.seasons[serie.seasons.size - 1].airDate != null) {
                    setAlarms(serie.seasons[serie.seasons.size - 1], serie.name)
                }
            }
        })
    }

    private fun setAlarms(season: Season, name: String) {
        val isAlarmActive =
            getPendingIntent(season, name, PendingIntent.FLAG_NO_CREATE) != null
        if (!isAlarmActive) {
            val pendingIntent =
                getPendingIntent(season, name, PendingIntent.FLAG_UPDATE_CURRENT)
            val result: IntArray
            try {
                result = Arrays.stream(season.airDate.split("-".toRegex()).toTypedArray())
                    .mapToInt(Integer::parseInt).toArray()
                val seasonDate: Date = getSeasonDate(result)
                val todayDate: Date = getTodayDate()
                if (seasonDate.after(todayDate) || seasonDate == todayDate) {
//                try {
//                    pendingIntent.send();
//                } catch (PendingIntent.CanceledException e) {
//                    e.printStackTrace();
//                }
                    val alarmManager =
                        getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager[AlarmManager.RTC_WAKEUP, seasonDate.time] = pendingIntent
                }
            } catch (ex: NumberFormatException) {
                FirebaseCrashlytics.getInstance()
                    .log("MAIN ACTIVITY - setAlarms() - Variable: " + season.airDate)
                FirebaseCrashlytics.getInstance().recordException(ex)
            }
        }
    }

    private fun getPendingIntent(season: Season, name: String, flag: Int): PendingIntent? {
        return PendingIntent.getBroadcast(
            this@MainActivity,
            System.currentTimeMillis().toInt(),
            Intent(this@MainActivity, Receiver::class.java)
                .setAction(ACTION_ALARM_RECEIVER + season.id)
                .putExtra(SEASON_ID_EXTRA, season.id)
                .putExtra(SERIE_NAME_EXTRA, name)
                .putExtra(SEASON_NUMBER_EXTRA, season.seasonNumber), flag
        )
    }

    private fun cancelAlarm(season: Season, alarmManager: AlarmManager) {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            season.id,
            Intent(
                this@MainActivity,
                Receiver::class.java
            ).setAction(Receiver.ACTION_ALARM_RECEIVER),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun getTodayDate(): Date {
        val today: Calendar = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 12)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        return today.time
    }

    private fun getSeasonDate(result: IntArray): Date {
        val seasonDate: Calendar =
            GregorianCalendar(result[0], result[1] - 1, result[2])
        seasonDate.set(Calendar.HOUR_OF_DAY, 12)
        return seasonDate.time
    }

    override fun onBackPressed() {
        startPos = 0
        super.onBackPressed()
    }

    private fun setNavigationView() {
        bottom_nav_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> goToHome()
                R.id.navigation_search -> goToSearch()
                R.id.navigation_fav -> goToFavs()
                R.id.navigation_profile -> goToProfile()
                else -> {
                    goToHome()
                }
            }
            startPos = newPos
            true
        }
    }

    private fun goToProfile() {
        newPos = 3
        if (startPos < newPos) navController.navigate(R.id.action_global_navigation_profile_left)
        else navController.navigate(R.id.action_global_navigation_profile)
    }

    private fun goToFavs() {
        newPos = 2
        when {
            startPos < newPos -> navController.navigate(R.id.action_global_navigation_fav_left)
            newPos < startPos -> navController.navigate(R.id.action_global_navigation_fav_right)
            else -> navController.navigate(R.id.action_global_navigation_fav_left)
        }
    }

    private fun goToSearch() {
        newPos = 1
        when {
            startPos < newPos -> navController.navigate(R.id.action_global_navigation_search_to_left)
            newPos < startPos -> navController.navigate(R.id.action_global_navigation_search_to_right)
            else -> navController.navigate(R.id.action_global_navigation_search)
        }
    }

    private fun goToHome() {
        newPos = 0
        if (startPos == newPos) navController.navigate(R.id.action_global_navigation_home)
        else navController.navigate(R.id.action_global_navigation_home_right)
    }
}
