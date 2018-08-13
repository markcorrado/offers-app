package com.corrado.offersapp

import android.arch.persistence.room.*

@Dao
interface OfferDataDao {
    @Query("SELECT * from offerData")
    fun getAll(): List<OfferData>

    @Query("SELECT * FROM offerData WHERE id LIKE :id LIMIT 1")
    fun getOfferDataById(id: Long): OfferData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(offerData: OfferData)

    @Query("DELETE from offerData")
    fun deleteAll()

    @Update
    fun updateOfferData(offerData: OfferData)
}