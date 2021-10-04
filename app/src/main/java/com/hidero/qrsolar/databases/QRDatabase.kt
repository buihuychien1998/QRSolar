package com.hidero.qrsolar.databases

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.hidero.qrsolar.daos.HistoryDao
import com.hidero.qrsolar.daos.MyQRDao
import com.hidero.qrsolar.daos.QRTextDao
import com.hidero.qrsolar.entities.History
import com.hidero.qrsolar.entities.MyQR
import com.hidero.qrsolar.entities.QRText


@Database(entities = [QRText::class, MyQR::class, History::class], version = 4)
abstract class QRDatabase : RoomDatabase() {
    companion object {
        private var instance: QRDatabase? = null
        fun getInstance(context: Context): QRDatabase {
            if (instance == null) {
                synchronized(QRDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            QRDatabase::class.java, "qr_database"
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance!!
        }
    }

    abstract fun qrTextDao(): QRTextDao
    abstract fun myQRDao(): MyQRDao
    abstract fun historyDao(): HistoryDao


}