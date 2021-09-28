package ru.voodster.currencyexchangetest.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import ru.voodster.currencyexchangetest.CurrencyViewModel



@Composable
fun SortScreen(viewModel: CurrencyViewModel,navHostController: NavHostController){
        Column(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(2f),contentAlignment = Alignment.Center) {
                Text(text = "Sort by", fontSize = 50.sp,textAlign = TextAlign.Center)
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(10f)
                    .padding(20.dp)) {
                Column(verticalArrangement = Arrangement.SpaceEvenly,horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()) {
                    CurrencyViewModel.SortBy.values().forEach { sortBy ->
                        SortButton(sortBy = sortBy) {
                            viewModel.setSortBy(it)
                            navHostController.popBackStack()
                        }
                    }
                }
            }
        }
}

@Composable
fun SortButton(sortBy: CurrencyViewModel.SortBy,onClick:(CurrencyViewModel.SortBy)->Unit){
    Text(text = sortBy.type+" "+sortBy.direction,Modifier.padding(10.dp).clickable { onClick(sortBy) },fontSize = 30.sp)
}