package com.example.pexels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pexels.core.nav.BottomNavigationBar
import com.example.pexels.presentation.splashScreen.SplashViewModel
import com.example.pexels.core.theme.PexelsTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen=installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition { splashViewModel.isLoading.value }
        setContent {
            PexelsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BottomNavigationBar(innerPadding)
                }
            }
        }
    }
}
