package ru.voodster.currencyexchangetest.compose


import NavigationScreens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.voodster.currencyexchangetest.CurrencyViewModel
import ru.voodster.currencyexchangetest.model.CurrencyModel
import ru.voodster.currencyexchangetest.ui.theme.CurrencyExchangeTestTheme

const val TAG = "PopularScreen"


@Composable
fun PopularScreen(viewModel: CurrencyViewModel, navHostController: NavHostController) {
    val list = viewModel.currencyList.collectAsState()
    CurrencyScreen(list.value, list.value,
        onSort = {
            navHostController.navigate(NavigationScreens.SORT.route)
        }, onAddFav = { id->
            viewModel.changeFav(id)
        }, onSelectBaseCurrency = {id->
            viewModel.setBase(id)
        },
        viewModel.base.id
    )
}

@Composable
fun FavoriteScreen(viewModel: CurrencyViewModel, navHostController: NavHostController) {
    val list = viewModel.currencyList.collectAsState()
    CurrencyScreen(
        list.value, list.value.filter { it.fav },
        onSort = {
            navHostController.navigate(NavigationScreens.SORT.route)
        },
        onAddFav = {id->
            viewModel.changeFav(id)
        },
        onSelectBaseCurrency = {id->
            viewModel.setBase(id)
        },
        viewModel.base.id
    )
}


@Composable
fun CurrencyScreen(
    currencyList: List<CurrencyModel>, contentList: List<CurrencyModel>,
    onSort: () -> Unit, onAddFav: (id: Int) -> Unit,
    onSelectBaseCurrency: (id: Int) -> Unit, baseCurrencyId: Int
) {
    var sortedList by remember { mutableStateOf(contentList) }
    Scaffold(Modifier.fillMaxSize(), backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopBar(
                list = currencyList, baseCurrencyId = baseCurrencyId,
                onSelectCurrency = { model ->
                    sortedList = contentList.map {
                        CurrencyModel(it.name, it.value / model.value, it.fav, it.id)
                    }
                    onSelectBaseCurrency(model.id)
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
            CurrencyList(list = sortedList, onClickFav = { onAddFav(it) })
        }
    }
}

@Composable
fun CurrencyList(list: List<CurrencyModel>, onClickFav: (id: Int) -> Unit) {
    LazyColumn(Modifier.fillMaxWidth()) {
        list.forEach {
            item { CurrencyItem(model = it, onAddFav = { onClickFav(it.id) }) }
        }
    }
}

@Composable
fun CurrencyItem(model: CurrencyModel, onAddFav: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Box {
            Text(text = model.name, textAlign = TextAlign.Center, fontSize = 30.sp)
        }
        Box {
            Text(
                text = String.format("%.2f", model.value),
                textAlign = TextAlign.Left,
                fontSize = 30.sp
            )
        }
        Box {
            var click by remember { mutableStateOf(model.fav) }
            ClickFavIcon(fav = click) {
                onAddFav()
                click = !click
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
    Icon(imageVector = image, "", Modifier.clickable { onAddFav() })
}


@Composable
fun TopBar(
    list: List<CurrencyModel>,
    baseCurrencyId: Int,
    onSelectCurrency: (model: CurrencyModel) -> Unit,
    onSort: () -> Unit
) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primaryVariant) {
        Box(Modifier.weight(8f)) {
            CurrencySelector(list = list, onSelect = { onSelectCurrency(it) }, baseCurrencyId)
        }
        Button(onClick = { onSort() }, Modifier.weight(2f)) {
            Text(text = "SORT", color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Composable
fun CurrencySelector(
    list: List<CurrencyModel>,
    onSelect: (model: CurrencyModel) -> Unit,
    baseCurrencyId: Int
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedString by remember { mutableStateOf(list[baseCurrencyId].name) }
    Row(
        Modifier
            .clickable { // Anchor view
                expanded = !expanded
            }) {
        Text(text = selectedString)
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            list.forEach {
                DropdownMenuItem(onClick = {
                    selectedString = it.name
                    onSelect(it)
                    expanded = false
                }) {
                    SelectableItem(name = it.name)
                }
            }
        }
        Icon(imageVector = Icons.Filled.ArrowDropDown, "")
    }
}

@Composable
fun SelectableItem(name: String) {
    Text(text = name, color = MaterialTheme.colors.onPrimary, fontSize = 20.sp)
}


@Preview
@Composable
fun CurrencyScreenPreview() {
    val viewModel = CurrencyViewModel()
    CurrencyExchangeTestTheme {
        CurrencyScreen(
            viewModel.mockList,
            viewModel.mockList,
            onSort = {},
            onAddFav = {},
            onSelectBaseCurrency = {},
            baseCurrencyId = 0
        )
    }
}
