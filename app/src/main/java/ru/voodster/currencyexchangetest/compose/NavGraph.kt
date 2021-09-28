package ru.voodster.currencyexchangetest.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.voodster.currencyexchangetest.CurrencyViewModel

@Composable
fun NavGraph(
    innerPadding: PaddingValues,
    viewModel: CurrencyViewModel,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationScreens.POPULAR.route
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(NavigationScreens.POPULAR.route ) {
            Scaffold {
            PopularScreen(viewModel = viewModel,navController)
            }
        }
        composable(NavigationScreens.FAVORITE.route ) {
            Scaffold {
                FavoriteScreen(viewModel = viewModel,navController)
            }
        }
        composable(NavigationScreens.SORT.route){
            Scaffold() {
                SortScreen(viewModel,navController)
            }
        }
    }
}

