package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsapp.data.Source
import com.example.newsapp.ui.theme.NewsAppTheme


class SourcesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchTerm = intent.getStringExtra("SEARCH_TERM") ?: "No search term"

        setContent {
            NewsAppTheme {
                SourcesScreen(searchTerm = searchTerm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesScreen(
    searchTerm: String,
    viewModel: SourcesViewModel = viewModel()
)  {
    val sources by viewModel.sources.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    val categories = listOf("General", "Technology", "Business", "Sports", "Health", "Science", "Entertainment")
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    LaunchedEffect(selectedCategory) {
        viewModel.fetchSources(selectedCategory)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Search for: '$searchTerm'", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val intent = Intent(context, ResultsActivity::class.java).apply {
                putExtra("SEARCH_TERM", searchTerm)
            }
            context.startActivity(intent)
        }) {
            Text("Skip Source Selection")
        }
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedCategory,
                onValueChange = {},
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                categories.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedCategory = selectionOption
                            expanded = false
                        },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sources) { source ->
                        SourceItem(source = source, onClick = {
                            val intent = Intent(context, ResultsActivity::class.java).apply {
                                putExtra("SEARCH_TERM", searchTerm)
                                putExtra("SOURCE_ID", source.id)
                                putExtra("SOURCE_NAME", source.name)
                            }
                            context.startActivity(intent)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun SourceItem(source: Source, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = source.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = source.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SourcesScreenPreview() {
    NewsAppTheme {
        SourcesScreen(searchTerm = "Android")
    }
}