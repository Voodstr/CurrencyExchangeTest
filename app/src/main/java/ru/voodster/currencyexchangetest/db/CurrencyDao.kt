package ru.voodster.currencyexchangetest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM CurrencyTable ")
    fun getCurrencyList(): Single<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg films: CurrencyEntity)

    @Query("SELECT base FROM AdditionalData")
    fun getBase():Single<String>

    @Query("SELECT id FROM CurrencyTable where fav")
    fun getFavoritesIds():Single<List<String>>
}