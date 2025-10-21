package com.example.newsapp.data

data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

data class Article(
    val source: SourceInfo,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

data class SourceInfo(
    val id: String?,
    val name: String
)

data class SourcesApiResponse(
    val status: String,
    val sources: List<Source>
)

data class Source(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
)