package com.nikhil.githubsearchapp.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun WebViewScreen(url: String) {
    val context = LocalContext.current
    LaunchedEffect(url) {
        val intent = Intent(context, WebViewActivity::class.java).apply {
            putExtra("url", url)
        }
        context.startActivity(intent)
    }
}