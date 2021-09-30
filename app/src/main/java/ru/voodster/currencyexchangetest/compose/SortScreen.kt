package ru.voodster.currencyexchangetest.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.voodster.currencyexchangetest.CurrencyViewModel
import ru.voodster.currencyexchangetest.R


@Composable
fun SortScreen(viewModel: CurrencyViewModel, navHostController: NavHostController) {
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(2f), contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.SortBy),
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .weight(8f)
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.byAlpha), fontSize = 30.sp)
                SortButton(text = stringResource(id = R.string.Ascending),
                    sortBy = CurrencyViewModel.SortBy.AlphaAsc,
                    onClick = { viewModel.setSortBy(CurrencyViewModel.SortBy.AlphaAsc) })
                SortButton(text = stringResource(id = R.string.Descending),
                    sortBy = CurrencyViewModel.SortBy.AlphaDesc,
                    onClick = { viewModel.setSortBy(CurrencyViewModel.SortBy.AlphaAsc) })
                Text(text = stringResource(id = R.string.byValue), fontSize = 30.sp)
                SortButton(text = stringResource(id = R.string.Ascending),
                    sortBy = CurrencyViewModel.SortBy.ValueAsc,
                    onClick = { viewModel.setSortBy(CurrencyViewModel.SortBy.ValueAsc) })
                SortButton(text = stringResource(id = R.string.Descending),
                    sortBy = CurrencyViewModel.SortBy.ValuesDesc,
                    onClick = { viewModel.setSortBy(CurrencyViewModel.SortBy.ValuesDesc) })
            }
        }
        Box(
            Modifier
                .weight(2f)
                .fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(onClick = { navHostController.popBackStack() }) {
                Text(text = stringResource(id = R.string.closeSort), fontSize = 30.sp)
            }
        }
    }
}

@Composable
fun SortButton(
    text: String,
    sortBy: CurrencyViewModel.SortBy,
    onClick: (CurrencyViewModel.SortBy) -> Unit
) {
    Text(
        text = text,
        Modifier
            .padding(10.dp)
            .clickable { onClick(sortBy) },
        fontSize = 25.sp
    )
}