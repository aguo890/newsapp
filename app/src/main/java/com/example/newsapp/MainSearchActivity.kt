package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsapp.ui.theme.NewsAppTheme

class MainSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                MainSearchScreen()
            }
        }
    }
}

@Composable
fun MainSearchScreen() {
    var searchTerm by remember { mutableStateOf("") }
    val isSearchButtonEnabled = searchTerm.isNotBlank()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("News Search", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = searchTerm,
            onValueChange = { searchTerm = it },
            label = { Text("Enter a search term") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* goto SourcesActivity */ },
            enabled = isSearchButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* goto to MapsActivity  */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Local News")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* go to TopHeadlinesActivity  */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Top Headlines")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainSearchScreenPreview() {
    NewsAppTheme {
        MainSearchScreen()
    }
}