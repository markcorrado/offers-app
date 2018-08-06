package com.corrado.offersapp

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "offerData")
data class OfferData(@PrimaryKey(autoGenerate = true) var id: Long?,
                     @ColumnInfo(name = "url") var url: String,
                     @ColumnInfo(name = "name") var offerName: String,
                     @ColumnInfo(name = "description") var offerDescription: String,
                     @ColumnInfo(name = "terms") var terms: String,
                     @ColumnInfo(name = "current_value") var currentValue: String
                     )
{
    constructor():this(null, "", "", "", "", "")
}