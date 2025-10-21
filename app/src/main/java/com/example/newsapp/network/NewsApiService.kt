package com.example.newsapp.network

import com.example.newsapp.data.NewsApiResponse
import com.example.newsapp.data.SourcesApiResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines/sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String,
        @Query("category") category: String
    ): Response<SourcesApiResponse>

    @GET("v2/everything")
    suspend fun getEverything(
        @Query("apiKey") apiKey: String,
        @Query("q") searchTerm: String,
        @Query("sources") sources: String? = null,
        @Query("language") language: String = "en"
    ): Response<NewsApiResponse>

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("category") category: String,
        @Query("language") language: String = "en",
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20
    ): Response<NewsApiResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://newsapi.org/"

    val instance: NewsApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(NewsApiService::class.java)
    }
}