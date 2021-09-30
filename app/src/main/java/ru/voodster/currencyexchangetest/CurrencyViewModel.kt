package ru.voodster.currencyexchangetest

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.voodster.currencyexchangetest.api.ApiModel
import ru.voodster.currencyexchangetest.db.CurrencyEntity

const val TAG = "CurrencyViewModel"

class CurrencyViewModel : ViewModel() {
    companion object {
        const val API_KEY = "db2687100a7599d1d6e9cdee1167e3de"
    }

    enum class SortBy() {
        AlphaAsc,
        AlphaDesc,
        ValueAsc,
        ValuesDesc
    }

    private val component = DaggerCurrencyViewModelComponent.builder().build()
    private val api = component.api()
    val db = component.db()

    var refreshState = false
    private var sortListBy = SortBy.AlphaAsc

    var httpError = ""


    val currencySymbols = listOf("EUR", "USD", "RUB", "GBP", "CHF", "CAD", "AED", "BYN", "KZT")
    private fun symbolsToString(): String {
        var result = ""
        currencySymbols.forEach {
            result += "$it,"
        }
        result.dropLast(result.length)
        return result
    }


    private var mockModelList = List(currencySymbols.size) {
        CurrencyEntity(it, currencySymbols[it], 1f, false)
    }


    var basicList: MutableList<CurrencyEntity> = mockModelList.toMutableList()
    var base = basicList[0]

    private val currencyList: List<CurrencyEntity>
        get() = sort(sortListBy,
            List(basicList.size) {
                CurrencyEntity(
                    basicList[it].id,
                    basicList[it].name,
                    basicList[it].value / base.value,
                    basicList[it].fav,
                )
            })

    private val _showedList = MutableStateFlow(currencyList)
    val showedList = _showedList.asStateFlow()


    private fun update() {
        _showedList.update { currencyList }
        refreshState = false
    }


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

    private fun sort(sortBy: SortBy, list: List<CurrencyEntity>) =
        when (sortBy) {
            SortBy.ValueAsc -> list.sortedBy { it.value }
            SortBy.ValuesDesc -> list.sortedByDescending { it.value }
            SortBy.AlphaAsc -> list.sortedBy { it.name }
            SortBy.AlphaDesc -> list.sortedByDescending { it.name }
        }

    private fun updateValues(apiModel: ApiModel) {
        if (apiModel.success){
            basicList.forEach {
                it.value = apiModel.rates[it.name] ?: 1f
            }
        }
    }

    fun refresh() {
        refreshState = true
        api.getLatest(API_KEY, "EUR", symbolsToString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                Log.d(TAG, "apiModel: $result")
                updateValues(result)
                saveToDb()
                update()
            }, { error ->
                httpError = error.localizedMessage ?: "UnknownError"
            })
    }

    fun saveToDb() {
        db.queryExecutor.execute {
            db.getDao().insertAll(*basicList.toTypedArray())
        }
    }

    private fun getFromDb() {
        refreshState = true
        db.queryExecutor.execute {
            db.getDao().getCurrencyList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    Log.d(TAG, "getFromDb: $result")
                    updateValuesFromDb(result)
                }, { error ->
                    refresh()
                    httpError = error.localizedMessage ?: "UnknownError"
                })
        }
    }

    private fun updateValuesFromDb(list: List<CurrencyEntity>) {
        if (list.isNotEmpty()) {
            basicList.clear()
            basicList.addAll(list)
            setBase(0)
            update()
        } else refresh()
    }


    init {
        getFromDb()
        refresh()
    }
}