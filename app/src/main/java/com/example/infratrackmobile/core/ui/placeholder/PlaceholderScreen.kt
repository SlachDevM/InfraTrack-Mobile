package com.example.infratrackmobile.core.ui.placeholder

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PlaceholderScreen(name: String = "Feature") {
    Text(text = "InfraTrack Architecture Placeholder: $name")
}
