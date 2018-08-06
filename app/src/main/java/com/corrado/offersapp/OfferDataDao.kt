package com.corrado.offersapp

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface OfferDataDao {
    @Query("SELECT * from offerData")
    fun getAll(): List<OfferData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(offerData: OfferData)

}