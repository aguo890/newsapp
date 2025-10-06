package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsapp.ui.theme.NewsAppTheme

data class FakeSource(val name: String, val description: String)

class SourcesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                SourcesScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourcesScreen() {
    val fakeSources = listOf(
        FakeSource("TechWeekly", "Your daily dose of tech news."),
        FakeSource("Finance Hub", "All about markets and money."),
        FakeSource("Global Sports", "Scores and highlights from around the world."),
        FakeSource("Health Matters", "Tips for a healthier lifestyle.")
    )

    val categories = listOf("General", "Technology", "Business", "Sports")
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Search for: 'Android'", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { /* Does nothing for now */ }) {
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

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(fakeSources) { source ->
                SourceItem(source = source)
            }
        }
    }
}

@Composable
fun SourceItem(source: FakeSource) {
    Card(modifier = Modifier.fillMaxWidth()) {
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
        SourcesScreen()
    }
}