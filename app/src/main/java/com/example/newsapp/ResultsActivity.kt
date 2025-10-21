package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsapp.ui.theme.NewsAppTheme

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchTerm = intent.getStringExtra("SEARCH_TERM") ?: "No search term provided"
        val sourceId = intent.getStringExtra("SOURCE_ID") // Can be null
        val sourceName = intent.getStringExtra("SOURCE_NAME") // For the title

        setContent {
            NewsAppTheme {
                val factory = ResultsViewModelFactory(searchTerm, sourceId)
                ResultsScreen(
                    sourceName = sourceName,
                    viewModel = viewModel(factory = factory)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    sourceName: String?,
    viewModel: ResultsViewModel
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = if (sourceName != null) {
                        "$sourceName for '${viewModel.searchTerm}'"
                    } else {
                        "Results for '${viewModel.searchTerm}'"
                    }
                    Text(text = title)
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(state.articles) { article ->
                        ArticleRow(article = article)
                    }
                }
            }
        }
    }
}