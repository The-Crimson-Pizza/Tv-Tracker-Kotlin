package com.thecrimsonpizza.tvtrackerkotlin.core.utils

object GlobalConstants {
    const val MY_CONST = "something"

    //    API
    const val API_KEY_STRING = "api_key"
    const val API_KEY = "18f61adb80d286bb036df43e60d7aae6"

    //    URLS
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_WEB_MOVIE = "https://www.themoviedb.org/movie/"
    const val BASE_URL_WEB_PERSON = "https://www.themoviedb.org/person/"
    const val BASE_URL_WEB_TV = "https://www.themoviedb.org/tv/"
    const val BASE_URL_IMAGES_POSTER = "https://image.tmdb.org/t/p/w300/"
    const val BASE_URL_IMAGES_PORTRAIT = "https://image.tmdb.org/t/p/w185/"
    const val BASE_URL_IMAGES_BACK = "https://image.tmdb.org/t/p/w780/"
    const val BASE_URL_IMAGES_NETWORK = "https://image.tmdb.org/t/p/w92/"
    const val BASE_URL_INSTAGRAM = "https://www.instagram.com/"
    const val BASE_URL_INSTAGRAM_U = "https://www.instagram.com/_u/"
    const val BASE_URL_YOUTUBE = "http://www.youtube.com/watch?v="
    const val BASE_URL_IMDB_PERSON = "https://www.imdb.com/name/"
    const val BASE_URL_IMDB_TITLE = "https://www.imdb.com/title/"

    //    ID'S
    const val ID_SERIE = "id_serie"
    const val ID_ACTOR = "id_actor"
    const val ID_SEASON = "id_season"
    const val ID_GENRE = "id_genre"
    const val ID_NETWORK = "id_network"

    //    API CALLS
    const val GET_SERIE_API_EXTRAS = "credits,similar,external_ids"
    const val GET_PEOPLE_API_EXTRAS = "tv_credits,movie_credits,external_ids"
    const val POP_DESC = "popularity.desc"
    const val TRAILER = "Trailer"
    const val MADRID = "Madrid"

    //    DATE FORMAT
    const val FORMAT_DEFAULT = "yyyy-MM-dd"
    const val FORMAT_LONG = "EEE dd, MMMM yyyy"
    const val FORMAT_YEAR = "yyyy"
    const val FORMAT_HOURS = "HH:mm"
    const val FORMAT_MINUTES = "%d:%02d"

    //    SHARED PREFERENCES
    const val FAV_TEMP_DATA = "TEMP_DATA"
    const val MY_PREFS = "myPrefs"
    const val FIRST_OPENED = "first_opened"

    //    INTENT KEYS
    const val URL_WEB_VIEW = "URL"
    const val ID = "id"
    const val DATA = "data"
    const val BASIC_SERIE_POSTER_PATH = "basic-serie-poster-path"
    const val BASIC_PERSON_POSTER_PATH = "basic-person-poster-path"
    const val SEASON_ID_EXTRA = "SEASON_ID"
    const val SERIE_NAME_EXTRA = "SERIE_NA"
    const val SEASON_NUMBER_EXTRA = "SEASON_NUMBER"
    const val TYPE_FRAGMENT = "TYPE"


    //    NOTIFICACIONES
    const val NEW_SEASON_NOTIFICATION_BUNDLE_CHANNEL_ID = "new_season_notification_bundle_channel"
    const val NEW_SEASON_NOTIFICATION_BUNDLE_CHANNEL_NAME =
        "New Season Notification Bundle Channel"
    const val NEW_SEASON_NOTIFICATION_CHANNEL_ID = "new_season_notification_channel"
    const val NEW_SEASON_NOTIFICATION_CHANNEL_NAME = "New Season Notification Channel"
    const val GROUP_KEY_SEASON_NEW = "group_key_season_new"

    //    OTHERS
    const val GENRE = "genre"
    const val NETWORKS = "network"
    const val COM_INSTAGRAM_ANDROID = "com.instagram.android"
    const val TEXT_PLAIN = "text/plain"
    const val SEASON_EPISODE_FORMAT = "%02dx%02d - %s"
    const val YOUTUBE = "YouTube"
    const val ES = "es-ES"


    const val EMPTY_STRING = ""

    //    TBC
    const val ACTOR_TRANSITION = "actor_transition"
    const val SERIE_TRANSITION = "serie_transition"

}