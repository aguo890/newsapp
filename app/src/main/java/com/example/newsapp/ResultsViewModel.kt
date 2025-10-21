package com.example.newsapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.Article
import com.example.newsapp.network.RetrofitClient
import kotlinx.coroutines.launch

data class ResultsUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ResultsViewModel(
    val searchTerm: String,
    val sourceId: String?
) : ViewModel() {

    var uiState by mutableStateOf(ResultsUiState())
        private set

    init {
        fetchResults()
    }

    private fun fetchResults() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val response = RetrofitClient.instance.getEverything(
                    apiKey = BuildConfig.NEWS_API_KEY,
                    searchTerm = searchTerm,
                    sources = sourceId
                )
                if (response.isSuccessful) {
                    uiState = uiState.copy(
                        isLoading = false,
                        articles = response.body()?.articles ?: emptyList()
                    )
                } else {
                    uiState = uiState.copy(isLoading = false, error = "API Error: ${response.message()}")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = "Network Error: ${e.message}")
            }
        }
    }
}


class ResultsViewModelFactory(
    private val searchTerm: String,
    private val sourceId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultsViewModel(searchTerm, sourceId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}