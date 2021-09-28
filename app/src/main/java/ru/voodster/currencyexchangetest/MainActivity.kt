package ru.voodster.currencyexchangetest

import CurrencyExchangeApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.voodster.currencyexchangetest.ui.theme.CurrencyExchangeTestTheme

class MainActivity : ComponentActivity() {

    private val viewModel:CurrencyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyExchangeTestTheme {
                CurrencyExchangeApp(currencyViewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyExchangeTestTheme {
        Greeting("Android")
    }
}