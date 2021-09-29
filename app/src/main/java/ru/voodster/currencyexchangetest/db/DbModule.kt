package ru.voodster.currencyexchangetest.db

import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.voodster.currencyexchangetest.App
import javax.inject.Singleton

@Module
class DbModule {


    companion object{
        const val DATABASE_NAME = "currency.db"
    }

    @Singleton
    @Provides
    fun provideDatabase(): CurrencyRoomDatabase {
        return Room.databaseBuilder(
            App.instance!!.applicationContext,
            CurrencyRoomDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}