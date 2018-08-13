package com.corrado.offersapp

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "offerData")
data class OfferData(@PrimaryKey(autoGenerate = true) var id: Long?,
                     @ColumnInfo(name = "url") var url: String?,
                     @ColumnInfo(name = "name") var name: String,
                     @ColumnInfo(name = "description") var description: String,
                     @ColumnInfo(name = "terms") var terms: String,
                     @SerializedName("current_value") @ColumnInfo(name = "current_value") var currentValue: String,
                     @ColumnInfo(name = "favorite") var isFavorite: Boolean
                     )
{
    constructor():this(null, "", "", "", "", "", false)
}