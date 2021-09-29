package ru.voodster.currencyexchangetest

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.voodster.currencyexchangetest.api.ApiModel
import ru.voodster.currencyexchangetest.model.CurrencyModel

const val TAG = "CurrencyViewModel"

class CurrencyViewModel : ViewModel() {
    companion object {
        const val API_KEY = "db2687100a7599d1d6e9cdee1167e3de"
    }


    enum class SortBy(val type: String, val direction: String) {
        AlphaAsc("Alphabet", "Ascending"),
        AlphaDesc("Alphabet", "Descending"),
        ValueAsc("Value", "Ascending"),
        ValuesDesc("Value", "Descending")
    }

    private val component = DaggerCurrencyViewModelComponent.builder().build()
    val api = component.api()
    val db = component.db()

    var refreshState = false
    private var sortListBy = SortBy.AlphaAsc


    val currencySymbols = listOf("EUR", "USD", "RUB", "GBP", "CHF", "CAD")
    private fun symbolsToString(): String {
        var result = ""
        currencySymbols.forEach {
            result += "$it,"
        }
        result.dropLast(result.length)
        return result
    }


    private var mockModelList = List(currencySymbols.size) {
        CurrencyModel(currencySymbols[it], (5..100).random().toFloat(), false, it)
    }
    var basicList: MutableList<CurrencyModel> = mockModelList.toMutableList()
    var base = basicList[0]

    val showedList: List<CurrencyModel>
        get() = sort(sortListBy,
            List(basicList.size) {
                CurrencyModel(
                    basicList[it].name,
                    basicList[it].value / base.value,
                    basicList[it].fav,
                    basicList[it].id
                )
            })

    private val _currencyList = MutableStateFlow(showedList) // private mutable state flow
    val currencyList = _currencyList.asStateFlow() // publicly exposed as read-only state flow

    fun setBase(id: Int) {
        refreshState = true
        base = basicList[id]
        update()
    }

    fun changeFav(id: Int) {
        basicList[id].fav = !basicList[id].fav
        update()
    }

    fun setSortBy(sortBy: SortBy) {
        sortListBy = sortBy
        update()
    }

    private fun sort(sortBy: SortBy, list: List<CurrencyModel>) =
        when (sortBy) {
            SortBy.ValueAsc -> list.sortedBy { it.value }
            SortBy.ValuesDesc -> list.sortedByDescending { it.value }
            SortBy.AlphaAsc -> list.sortedBy { it.name }
            SortBy.AlphaDesc -> list.sortedByDescending { it.name }
        }

    fun updateValues(apiModel: ApiModel) {
        basicList.forEach {
            Log.d(TAG,"before = ${it.value}")
            it.value = apiModel.rates[it.name] ?: 1f
            Log.d(TAG,"after = ${it.value}")
        }
    }

    fun refresh() {
        refreshState =true
        api.getLatest(API_KEY, currencySymbols[0], symbolsToString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                Log.d(TAG, "apiModel: $result")
                updateValues(result)
                update()
            }, { error ->
                //httpError = error.localizedMessage ?: "UnknownError"
                Log.d(TAG, "error: ${error.localizedMessage}")
            })
    }

    private fun update() {
        _currencyList.update { showedList }
        refreshState = false
    }

    init {
        refresh()
    }
}