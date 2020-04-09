package com.nicoqueijo.android.currencyconverter.kotlin.data

import com.nicoqueijo.android.currencyconverter.kotlin.model.ApiEndPoint
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API endpoint: https://openexchangerates.org/api/latest.json?app_id={app_id}
 */
interface RetrofitService {

    @GET("api/latest.json")
    suspend fun getExchangeRates(@Query("app_id") app_id: String): Response<ApiEndPoint>
}

object RetrofitFactory {
    private const val BASE_URL = "https://openexchangerates.org/"

    fun getRetrofitService(): RetrofitService {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
    }
}