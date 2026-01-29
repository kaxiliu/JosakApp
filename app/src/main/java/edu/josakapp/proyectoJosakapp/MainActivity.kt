package edu.josakapp.proyectoJosakapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import edu.josakapp.proyectoJosakapp.ui.navigation.NavigationHost
import edu.josakapp.proyectoJosakapp.ui.theme.AplicationNewTheme
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicationNewTheme{
                Surface {
                    val navController = rememberNavController()
                    NavigationHost(navController = navController)
                }
            }
        }
    }
}

