package com.indra.rimac.allMovies.presentacion.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.indra.rimac.allMovies.presentacion.navigation.Navigation.NavigationMain
import com.indra.rimac.allMovies.presentacion.theme.AllMoviesTheme
import com.indra.rimac.allMovies.presentacion.theme.ColorWhite
import com.indra.rimac.allMovies.presentacion.theme.Purple80
import com.indra.rimac.allMovies.presentacion.ui.composables.CustomToolbarLoginCompose
import com.indra.rimac.allMovies.presentacion.viewModels.NavigationViewModel
import com.indra.rimac.allMovies.utils.helpers.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val navigationViewModel: NavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val currentRoute by navigationViewModel.currentRoute.collectAsState()

            AllMoviesTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    if (currentRoute.idRoute == Constants.Routes.LOGIN) null
                    else
                        CustomToolbarLoginCompose(
                            title = currentRoute.title,
                            iconNavigation = Icons.AutoMirrored.Filled.ArrowBack,
                            iconNavigationColor = ColorWhite,
                            onClickNavigation = { navigationViewModel.onBack },
                            backgroundColor = Purple80
                        )
                }) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NavigationMain(
                            navController = navController,
                            startDestination = currentRoute.route,
                            navigationViewModel = navigationViewModel
                        )
                    }
                }
            }

            LaunchedEffect(currentRoute) {
                navController.navigate(currentRoute.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
}