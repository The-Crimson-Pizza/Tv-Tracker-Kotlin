package com.thecrimsonpizza.tvtrackerkotlin.app.ui.profile

import android.content.Context
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.EMPTY_STRING
import java.util.*

class StatsAdapter(val context: Context) {

    fun countSeries(followingList: MutableList<SerieResponse.Serie>) = followingList.size.toString()

    fun countTotalEpisodesWatched(followingList: MutableList<SerieResponse.Serie>): String {
        var contEpisodes = 0
        for (show in followingList) {
            for (season in show.seasons) {
                contEpisodes += season.episodes.count { it.visto }
            }
        }
        return contEpisodes.toString()
    }

    private fun convertToDaysHoursMinutes(timeInMinutes: Int): String {
        val minutesInYear = 60 * 24 * 365
        val year: Int = timeInMinutes / minutesInYear
        val days: Int = timeInMinutes / 24 / 60 % 365
        val hours: Int = timeInMinutes / 60 % 24
        val minutes: Int = timeInMinutes % 60
        return when {
            days == 0 -> {
                context.getString(R.string.hours_watched_total, hours, minutes)
            }
            year == 0 -> {
                context.getString(R.string.days_watched_total, days, hours, minutes)
            }
            else -> {
                context.getString(R.string.year_watched_total, year, days, hours, minutes)
            }
        }
    }

    fun mostWatchedTvShow(followingList: List<SerieResponse.Serie>): String? {
        val seriesMap = HashMap<String, Int>()
        for (tvShow in followingList)
            seriesMap[tvShow.name] = tvShow.seasons.flatMap { it.episodes }.count { it.visto }
        seriesMap.maxBy { it.value }?.key?.let {
            return it
        } ?: return EMPTY_STRING
    }

    fun countTimeEpisodesWatched(followingList: List<SerieResponse.Serie>): String? {
        var contTime = 0
        for (tvShow in followingList) {
            contTime += (tvShow.seasons.flatMap { it.episodes }.filter { it.visto }
                .groupBy { it.id }
                .mapValues { entry ->
                    entry.value
                        .map { getShowRuntime(tvShow) }
                        .sum()
                }).map { it.value }.sum()
        }
        return convertToDaysHoursMinutes(contTime)
    }

    private fun getShowRuntime(show: SerieResponse.Serie): Int {
        return show.episodeRunTime?.first() ?: 45
    }

    private fun getTopTenGenres(followingList: List<SerieResponse.Serie>): Map<String, Int>? {
        val genresMap = HashMap<String, Int>()
        for (show in followingList) {
            show.genres?.groupingBy { it.name }?.eachCount()
                ?.forEach { key, value ->
                    genresMap.merge(key, value) { t, u -> t + u }
                }
        }
        return genresMap.toList().sortedBy { (_, value) -> value }
            .reversed().take(10).toMap()
    }

    private fun getTopTenNetworks(followingList: List<SerieResponse.Serie>): Map<String, Int>? {
        val networksMap = HashMap<String, Int>()
        for (show in followingList) {
            show.networks?.groupingBy { it.name }?.eachCount()
                ?.forEach { key, value ->
                    networksMap.merge(key, value) { t, u -> t + u }
                }
        }
        return networksMap.toList().sortedBy { (_, value) -> value }
            .reversed().take(10).toMap()
    }

    fun getNetworksChartData(followingList: List<SerieResponse.Serie>): List<DataEntry> {
        val networkData = mutableListOf<DataEntry>()
        val countNetworks = getTopTenNetworks(followingList)
        for ((key, value) in countNetworks!!) {
            networkData.add(ValueDataEntry(key, value))
        }
        return networkData
    }

    fun getGenresChartData(followingList: List<SerieResponse.Serie>): List<DataEntry> {
        val genresData: MutableList<DataEntry> = ArrayList()
        val countGenres = getTopTenGenres(followingList)
        for ((key, value) in countGenres!!) {
            genresData.add(ValueDataEntry(key, value))
        }
        return genresData
    }
}

