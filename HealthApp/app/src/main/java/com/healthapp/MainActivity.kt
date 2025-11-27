package com.healthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.healthapp.navigation.HealthAppNavigation
import com.healthapp.ui.theme.HealthAppTheme
import com.healthapp.viewmodel.HealthAppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HealthApp()
                }
            }
        }
    }
}

@Composable
fun HealthApp(viewModel: HealthAppViewModel = viewModel()) {
    // Main app composable will be implemented with navigation
    HealthAppNavigation(viewModel)
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun HealthAppPreview() {
    HealthAppTheme {
        HealthApp()
    }
}