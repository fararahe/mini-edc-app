package id.faradyna.miniedcapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import id.faradyna.miniedcapp.ui.feature.activation.ActivationScreen
import id.faradyna.miniedcapp.ui.feature.home.HomeScreen
import id.faradyna.miniedcapp.ui.navigation.Screen
import id.faradyna.miniedcapp.ui.theme.MiniEDCMerchantAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniEDCMerchantAppTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()

                MainContent(uiState = uiState)
            }
        }
    }
}

@Composable
fun MainContent(uiState: MainUiState) {
    if (uiState.isActivated != null) {
        AppNavigation(
            startDestination = if (uiState.isActivated == true) Screen.Home else Screen.Activation
        )
    }
}

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Activation) {
            ActivationScreen(
                onActivationSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Activation) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home) {
            HomeScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MiniEDCMerchantAppTheme {
        MainContent(
            uiState = MainUiState(isActivated = true)
        )
    }
}
