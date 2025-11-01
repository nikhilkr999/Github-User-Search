package com.nikhil.githubsearchapp.ui.theme

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nikhil.githubsearchapp.R
import com.nikhil.githubsearchapp.data.GHRepo
import java.net.URLEncoder

private const val TAG = "SearchScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1117),
                        Color(0xFF161B22),
                        Color(0xFF1F6FEB)
                    )
                )
            )) {

        // 🔹 Background Image
//        Image(
//            painter = painterResource(id = R.drawable.github_back),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop
//        )

        // 🔹 Semi-transparent overlay for readability
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "GitHub User Search",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.95f),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            Text(
                text = "Search for a GitHub user and explore their repositories.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Username Input
            OutlinedTextField(
                value = state.username,
                onValueChange = {
                    viewModel.updateUsername(it)
                },
                label = { Text("Enter GitHub Username", color = Color.White.copy(alpha = 0.8f)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search Button
            Button(
                onClick = {
                    Log.d(TAG, "Search button clicked for user: ${state.username}")
                    viewModel.fetchRepos()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f))
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search", color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // State Handling
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.White
                    )
                }
                state.error != null -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
//                        Icon(
//                            Icons.Default.Clear,
//                            contentDescription = null,
//                            tint = Color.White.copy(alpha = 0.8f)
//                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "No results found",
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    if (state.repos.isNotEmpty()) {
                        OutlinedTextField(
                            value = state.filterQuery,
                            onValueChange = {
                                viewModel.updateFilter(it)
                            },
                            label = { Text("Filter by Name or ID", color = Color.White.copy(alpha = 0.8f)) },
//                            leadingIcon = { Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color.White) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                                cursorColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    AnimatedVisibility(
                        visible = state.filteredRepos.isNotEmpty(),
                        enter = fadeIn()
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.filteredRepos) { repo ->
                                RepoItem(repo, state.username) { url ->
                                    navController.navigate("webview/$url")
                                }
                            }
                        }
                    }

                    if (state.repos.isEmpty() && !state.isLoading && state.error == null) {
                        Text(
                            text = "Enter a username to start searching.",
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RepoItem(repo: GHRepo, user: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onClick(URLEncoder.encode(repo.repoURL, "UTF-8"))
            },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = repo.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.95f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Repo ID: ${repo.id}", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
            Text("Owner: $user", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
        }
    }
}
