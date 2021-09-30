package ru.voodster.currencyexchangetest.compose

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.voodster.currencyexchangetest.CurrencyViewModel
import ru.voodster.currencyexchangetest.R

@Composable
fun CurrencyExchangeApp(currencyViewModel: CurrencyViewModel) {

    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        val systemUiController = rememberSystemUiController()
        val statusBarColor = MaterialTheme.colors.primary
        SideEffect {
            systemUiController.setStatusBarColor(statusBarColor)
        }
        val navController = rememberNavController()

        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                BottomNavigationBar(
                    navController,
                    listOf(NavigationScreens.POPULAR, NavigationScreens.FAVORITE)
                )
            }
        )
        { innerPadding ->
            NavGraph(
                viewModel = currencyViewModel,
                navController = navController,
                innerPadding = innerPadding
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<NavigationScreens>) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val sections = remember { NavigationScreens.values() }
    val routes = remember { sections.map { it.route } }


    if (currentRoute in routes) {
        val currentSection = sections.first { it.route == currentRoute }
        BottomAppBar(
            elevation = 8.dp
        ) {
            items.forEach { section ->
                val selected = section == currentSection
                BottomNavigationItem(
                    icon = { Icon(section.icon, "contentDescription") },
                    label = { Text(stringResource(id = section.resourceId)) },
                    selected = selected,
                    alwaysShowLabel = false, // This hides the title for the unselected items
                    onClick = {
                        // This if check gives us a "singleTop" behavior where we do not create a
                        // second instance of the composable if we are already on that destination
                        if (currentRoute != section.route) {
                            navController.navigate(section.route)
                        }
                    }
                )
            }
        }
    }

}

enum class NavigationScreens(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    POPULAR("home/current", R.string.Popular, Icons.Filled.List),
    FAVORITE("home/table", R.string.Favorite, Icons.Filled.Favorite),
    SORT("sort", R.string.Sort, Icons.Filled.List)
}