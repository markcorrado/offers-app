package com.corrado.offersapp

import android.arch.persistence.room.*

@Dao
interface OfferDataDao {
    /**
      *  Get a List of all OfferData
     */
    @Query("SELECT * from offerData")
    fun getAll(): List<OfferData>

    /**
     * Return a OfferData with this ID.
     * Is there a better way to do this?
     */
    @Query("SELECT * FROM offerData WHERE id LIKE :id LIMIT 1")
    fun getOfferDataById(id: Long): OfferData

    /**
     * Insert an OfferData
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(offerData: OfferData)

    /**
     * Delete an OfferData
     */
    @Query("DELETE from offerData")
    fun deleteAll()

    /**
     * Update an OfferData
     */
    @Update
    fun updateOfferData(offerData: OfferData)
}