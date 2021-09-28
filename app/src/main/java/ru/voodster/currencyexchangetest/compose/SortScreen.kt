package ru.voodster.currencyexchangetest.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

enum class SortBy{
    AlphaAsc,
    AlphaDesc,
    ValueAsc,
    ValuesDesc
}

@Composable
fun SortScreen():SortBy{
    Scaffold() {
        Column() {
            Box() {
                Text(text = "Sort by",fontSize = 50.sp)
            }

        }
    }
return SortBy.AlphaAsc
}