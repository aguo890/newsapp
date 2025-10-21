package com.example.newsapp

// FIX #1: ADD THESE TWO IMPORTS
import android.content.Intent
import androidx.compose.ui.platform.LocalContext

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

    // FIX #2: GET THE CONTEXT HERE
    val context = LocalContext.current

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
            onClick = {
                // Now 'Intent' and 'context' are recognized!
                val intent = Intent(context, SourcesActivity::class.java).apply {
                    putExtra("SEARCH_TERM", searchTerm)
                }
                context.startActivity(intent)
            },
            enabled = isSearchButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(12.dp))


        Button(
            onClick = {
                val intent = Intent(context, MapsActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Local News")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                // Change this from the TODO comment
                val intent = Intent(context, TopHeadlinesActivity::class.java)
                context.startActivity(intent)
            },
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