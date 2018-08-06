package com.corrado.offersapp

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import java.security.AccessControlContext

@Database(entities = arrayOf(OfferData::class), version = 1)
abstract class OfferDataBase : RoomDatabase() {
    abstract fun offerDataDao(): OfferDataDao

    companion object {
        private var INSTANCE: OfferDataBase? = null

        fun getInstance(context: Context): OfferDataBase? {
            if (INSTANCE == null) {
                synchronized(OfferDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            OfferDataBase::class.java, "offer.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}