package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

/**Clase que servirá como plantilla para las pantallas de settings.*/

@OptIn( ExperimentalMaterial3Api::class)
@Composable
fun SettingsScaffold (title: String, screen: @Composable (PaddingValues) -> Unit) {
    Scaffold (topBar = {
        TopAppBar(
            title = { Text(text = title)}
        )
    }){  innerPadding -> screen(innerPadding)
    }
}