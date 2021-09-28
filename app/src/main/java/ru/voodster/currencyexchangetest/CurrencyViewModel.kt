package ru.voodster.currencyexchangetest

import androidx.lifecycle.ViewModel
import ru.voodster.currencyexchangetest.model.CurrencyModel

class CurrencyViewModel : ViewModel() {

    private val currencyList = listOf("USD", "RUB", "EUR", "GBP", "CHF", "GPY", "CAD")


    private val currencyModelList = List(currencyList.size) {
        CurrencyModel(currencyList[it], (1..30).random().toFloat(), false, it)
    }
    var base = currencyModelList[0]

    /*
    val mockList = List(currencyModelList.size) {
        CurrencyModel(
            currencyModelList[it].name,
            currencyModelList[it].value / base.value,
            currencyModelList[it].fav,
            currencyModelList[it].id
        )
    }

     */
    val mockList :List<CurrencyModel>
    get() = List(currencyModelList.size) {
        CurrencyModel(
            currencyModelList[it].name,
            currencyModelList[it].value / base.value,
            currencyModelList[it].fav,
            currencyModelList[it].id
        )
    }

    fun setBase(id:Int){
        base = currencyModelList[id]
    }

    fun changeFav(id: Int){
        currencyModelList[id].fav=!currencyModelList[id].fav
    }


}