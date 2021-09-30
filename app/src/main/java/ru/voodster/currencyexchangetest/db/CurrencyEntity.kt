package ru.voodster.currencyexchangetest.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "CurrencyTable")
@Parcelize
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = false) var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "value") var value: Float,
    @ColumnInfo(name = "fav") var fav: Boolean
) : Parcelable
