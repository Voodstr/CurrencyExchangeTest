package ru.voodster.currencyexchangetest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.voodster.currencyexchangetest.model.CurrencyModel

const val TAG = "CurrencyViewModel"

class CurrencyViewModel : ViewModel() {

    enum class SortBy(val type:String,val direction:String){
        AlphaAsc("Alphabet","Ascending"),
        AlphaDesc("Alphabet","Descending"),
        ValueAsc("Value","Ascending"),
        ValuesDesc("Value","Descending")
    }


    private val currencySymbols = listOf("EUR","USD", "RUB", "GBP", "CHF", "GPY", "CAD")


    private val currencyModelList = List(currencySymbols.size) {
        CurrencyModel(currencySymbols[it], (5..100).random().toFloat(), false, it)
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


    private var sortListBy = SortBy.AlphaAsc

    val mockList :List<CurrencyModel>
    get() = sort(sortListBy,
        List(currencyModelList.size) {
        CurrencyModel(
            currencyModelList[it].name,
            currencyModelList[it].value / base.value,
            currencyModelList[it].fav,
            currencyModelList[it].id
        )
    })


    private val _currencyList = MutableStateFlow(mockList) // private mutable state flow
    val currencyList = _currencyList.asStateFlow() // publicly exposed as read-only state flow


    fun setBase(id:Int){
        base = currencyModelList[id]
        _currencyList.update {mockList}
    }

    fun changeFav(id: Int){
        currencyModelList[id].fav=!currencyModelList[id].fav
        _currencyList.update {mockList}
    }

    fun setSortBy(sortBy: SortBy){
        sortListBy = sortBy
        _currencyList.update { mockList }
    }

    private fun sort(sortBy: SortBy, list: List<CurrencyModel>)=
        when(sortBy) {
            SortBy.ValueAsc -> list.sortedBy { it.value }
            SortBy.ValuesDesc -> list.sortedByDescending { it.value }
            SortBy.AlphaAsc -> list.sortedBy { it.name }
            SortBy.AlphaDesc -> list.sortedByDescending { it.name }
        }


}