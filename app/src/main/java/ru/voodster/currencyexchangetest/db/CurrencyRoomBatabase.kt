package ru.voodster.currencyexchangetest.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [CurrencyEntity::class,AdditionalData::class],version = 3,exportSchema = false)
abstract class CurrencyRoomDatabase:RoomDatabase() {
    abstract fun getDao():CurrencyDao
}