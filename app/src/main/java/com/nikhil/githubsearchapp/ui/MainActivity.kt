package com.nikhil.githubsearchapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nikhil.githubsearchapp.ui.theme.GitHubSearchAppTheme
import com.nikhil.githubsearchapp.ui.theme.SearchScreen
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubSearchAppTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "search"
                    ) {
                        composable("search") {
                            SearchScreen(navController)
                        }
                        composable("webview/{url}") {
                            backStackEntry ->
                            val url = URLDecoder.decode(backStackEntry.arguments?.getString("url") ?: "", "UTF-8")
                            WebViewScreen(url)
                        }
                    }
                }
            }
        }
    }
}