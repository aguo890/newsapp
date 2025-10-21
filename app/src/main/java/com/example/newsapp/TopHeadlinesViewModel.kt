package com.example.newsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.Article
import com.example.newsapp.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.min

data class TopHeadlinesUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val selectedCategory: String = "General"
)

class TopHeadlinesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TopHeadlinesUiState())
    val uiState = _uiState.asStateFlow()

    companion object {
        private const val PAGE_SIZE = 20
        private const val MAX_PAGES = 5
    }

    init {
        fetchHeadlines()
    }

    fun onCategoryChanged(newCategory: String) {
        _uiState.update {
            it.copy(
                selectedCategory = newCategory,
                currentPage = 1
            )
        }
        fetchHeadlines()
    }

    fun onNextPage() {
        if (_uiState.value.currentPage < _uiState.value.totalPages) {
            _uiState.update { it.copy(currentPage = it.currentPage + 1) }
            fetchHeadlines()
        }
    }

    fun onPreviousPage() {
        if (_uiState.value.currentPage > 1) {
            _uiState.update { it.copy(currentPage = it.currentPage - 1) }
            fetchHeadlines()
        }
    }

    private fun fetchHeadlines() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, articles = emptyList()) }
            try {
                val response = RetrofitClient.instance.getTopHeadlines(
                    apiKey = BuildConfig.NEWS_API_KEY,
                    category = _uiState.value.selectedCategory.lowercase(),
                    page = _uiState.value.currentPage,
                    pageSize = PAGE_SIZE
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    val articles = body?.articles ?: emptyList()
                    val totalResults = body?.totalResults ?: 0


                    val calculatedPages = ceil(totalResults.toDouble() / PAGE_SIZE).toInt()
                    val totalPages = min(calculatedPages, MAX_PAGES)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            articles = articles,
                            totalPages = if (totalPages > 0) totalPages else 1
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "API Error: ${response.message()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Network Error: ${e.message}") }
            }
        }
    }
}