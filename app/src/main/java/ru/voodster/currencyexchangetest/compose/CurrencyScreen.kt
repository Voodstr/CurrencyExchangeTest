package ru.voodster.currencyexchangetest.compose


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.voodster.currencyexchangetest.CurrencyViewModel
import ru.voodster.currencyexchangetest.R
import ru.voodster.currencyexchangetest.db.CurrencyEntity
import ru.voodster.currencyexchangetest.ui.theme.CurrencyExchangeTestTheme


@Composable
fun MainScreen(
    viewModel: CurrencyViewModel,
    navHostController: NavHostController,
    favoriteScreen: Boolean
) {
    val list = viewModel.showedList.collectAsState()
    CurrencyScreen(
        viewModel.currencySymbols, if (favoriteScreen) {
            list.value.filter { it.fav }
        } else list.value,
        onSort = {
            navHostController.navigate(NavigationScreens.SORT.route)
        },
        onAddFav = { id ->
            viewModel.changeFav(id)
        },
        onSelectBaseCurrency = { id ->
            viewModel.setBase(id)
        },
        viewModel.base.id,
        onRefresh = { viewModel.refresh() },
        isRefreshing = viewModel.refreshState
    )
}


@Composable
fun CurrencyScreen(
    currencyList: List<String>, contentList: List<CurrencyEntity>,
    onSort: () -> Unit, onAddFav: (id: Int) -> Unit,
    onSelectBaseCurrency: (id: Int) -> Unit, baseCurrencyId: Int,
    onRefresh: () -> Unit, isRefreshing: Boolean
) {
    Scaffold(Modifier.fillMaxSize(), backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopBar(
                list = currencyList, baseCurrencyId = baseCurrencyId,
                onSelectCurrency = { id ->
                    onSelectBaseCurrency(id)
                },
                onSort = {
                    onSort()
                },
            )
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            CurrencyList(
                list = contentList, onClickFav = { onAddFav(it) },
                onRefresh = { onRefresh() }, refresh = isRefreshing
            )
        }
    }
}

@Composable
fun CurrencyList(
    list: List<CurrencyEntity>, onClickFav: (id: Int) -> Unit,
    refresh: Boolean, onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refresh),
        onRefresh = { onRefresh() })
    {
        LazyColumn(Modifier.fillMaxWidth()) {
            list.forEach {
                item { CurrencyItem(entity = it, onAddFav = { onClickFav(it.id) }) }
            }
        }
    }
}

@Composable
fun CurrencyItem(entity: CurrencyEntity, onAddFav: () -> Unit) {
    Surface(
        elevation = 4.dp,
        modifier = Modifier.padding(3.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(5.dp,0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Text(text = entity.name, textAlign = TextAlign.Center, fontSize = 50.sp)
            }
            Box {
                Text(
                    text = String.format("%.3f", entity.value),
                    textAlign = TextAlign.Left,
                    fontSize = 30.sp
                )
            }
            Box {
                var click by remember { mutableStateOf(entity.fav) }
                ClickFavIcon(fav = click) {
                    onAddFav()
                    click = !click
                }
            }
        }
    }
}

fun addFav(fav: Boolean) = if (fav) {
    Icons.Outlined.Favorite
} else {
    Icons.Outlined.FavoriteBorder
}

@Composable
fun ClickFavIcon(fav: Boolean, onAddFav: () -> Unit) {
    val image = addFav(fav)
    Icon(imageVector = image, "",
        Modifier
            .size(40.dp)
            .clickable { onAddFav() })
}


@Composable
fun TopBar(
    list: List<String>,
    baseCurrencyId: Int,
    onSelectCurrency: (id: Int) -> Unit,
    onSort: () -> Unit
) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primaryVariant) {
        Row(
            Modifier
                .weight(8f)
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = stringResource(id = R.string.BaseCurrency), fontSize = 20.sp)
            CurrencySelector(list = list, onSelect = { onSelectCurrency(it) }, baseCurrencyId)
        }
        Button(onClick = { onSort() }, Modifier.weight(2f).padding(5.dp)) {
            Icon(imageVector = Icons.Default.List, contentDescription = "",Modifier.fillMaxSize())
        }
    }
}

@Composable
fun CurrencySelector(
    list: List<String>,
    onSelect: (id: Int) -> Unit,
    baseCurrencyId: Int
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedString by remember { mutableStateOf(list[baseCurrencyId]) }
    Row(
        Modifier
            .clickable { // Anchor view
                expanded = !expanded
            }) {
        Text(text = selectedString, color = MaterialTheme.colors.onPrimary, fontSize = 30.sp)
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            list.forEach {
                DropdownMenuItem(onClick = {
                    selectedString = it
                    onSelect(list.indexOf(it))
                    expanded = false
                }) {
                    SelectableItem(name = it)
                }
            }
        }
        Icon(imageVector = Icons.Filled.ArrowDropDown, "")
    }
}

@Composable
fun SelectableItem(name: String) {
    Text(text = name, color = MaterialTheme.colors.onPrimary, fontSize = 30.sp)
}


@Preview
@Composable
fun CurrencyScreenPreview() {
    val currencySymbols = listOf("EUR", "USD", "RUB", "GBP", "CHF", "CAD")
    val mockModelList = List(currencySymbols.size) {
        CurrencyEntity(it, currencySymbols[it], (5..100).random().toFloat(), false)
    }
    CurrencyExchangeTestTheme {
        CurrencyScreen(
            currencySymbols,
            mockModelList,
            onSort = {},
            onAddFav = {},
            onSelectBaseCurrency = {},
            baseCurrencyId = 0,
            onRefresh = {},
            isRefreshing = false
        )
    }
}
