package ru.voodster.currencyexchangetest.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "CurrencyTable")
@Parcelize
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = false) val id:String,
    @ColumnInfo(name = "value") val value:Float,
    @ColumnInfo(name = "fav") val fav:Boolean
): Parcelable

@Entity(tableName = "AdditionalData")
@Parcelize
data class AdditionalData(
    @PrimaryKey(autoGenerate = false) val base:String
):Parcelable
