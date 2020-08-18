package com.thecrimsonpizza.tvtrackerkotlin.app.data.remote

import com.thecrimsonpizza.tvtrackerkotlin.BuildConfig
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.API_KEY_STRING
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    private class TMDBInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()
            val url = request.url.newBuilder().addQueryParameter(API_KEY_STRING, BuildConfig.API_KEY).build();
            request = request.newBuilder().url(url).header("Accept", "application/json").build();
            return chain.proceed(request)
        }
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(TMDBInterceptor())
//        .addInterceptor(NoConnectionInterceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .retryOnConnectionFailure(false)
        .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
        .writeTimeout(30, TimeUnit.SECONDS) // write timeout
        .readTimeout(30, TimeUnit.SECONDS)
        .build();

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

    private fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

    val apiService: TmdbApi = buildService(TmdbApi::class.java)

}