package com.pocketkit.simplecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pocketkit.simplecalculator.ui.theme.SimpleCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val themeManager = ThemeManager(applicationContext)
        val factory = CalculatorViewModelFactory(themeManager)
        val viewModel = ViewModelProvider(this, factory)[CalculatorViewModel::class.java]

        setContent {
            val theme by viewModel.theme.collectAsState()
            val isSystemDark = isSystemInDarkTheme()
            val darkTheme = when (theme) {
                AppTheme.Light -> false
                AppTheme.Dark -> true
                AppTheme.System -> isSystemDark
            }

            SimpleCalculatorTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "calculator") {
                    composable("calculator") {
                        val state = viewModel.state
                        val buttonSpacing = 8.dp
                        CalculatorScreen(
                            state = state,
                            onAction = viewModel::onAction,
                            onHistoryClick = viewModel::onHistoryClick,
                            onThemeChange = viewModel::setTheme,
                            onAboutClick = { navController.navigate("about") },
                            currentTheme = theme,
                            buttonSpacing = buttonSpacing,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    composable("about") {
                        AboutScreen(
                            modifier = Modifier.fillMaxSize(),
                            currentTheme = theme
                        )
                    }
                }
            }
        }
    }
}