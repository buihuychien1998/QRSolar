package com.hidero.qrsolar.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.hidero.qrsolar.entities.QRText

@Dao
interface QRTextDao {
    @Insert
    fun insert(text: QRText)

    @Update
    fun update(text: QRText)

    @Delete
    fun delete(text: QRText)

    @Query("DELETE FROM qrtext")
    fun deleteAll()

    @Query("SELECT * FROM qrtext")
    fun getAll(): LiveData<List<QRText>>

}