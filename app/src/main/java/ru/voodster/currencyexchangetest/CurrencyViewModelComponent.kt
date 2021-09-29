package ru.voodster.currencyexchangetest

import dagger.Component
import ru.voodster.currencyexchangetest.api.ApiModule
import ru.voodster.currencyexchangetest.api.CurrencyApi
import ru.voodster.currencyexchangetest.db.CurrencyRoomDatabase
import ru.voodster.currencyexchangetest.db.DbModule
import javax.inject.Singleton

@Component(modules = [ApiModule::class,DbModule::class])
@Singleton
interface CurrencyViewModelComponent {
    fun api():CurrencyApi
    fun db():CurrencyRoomDatabase
}