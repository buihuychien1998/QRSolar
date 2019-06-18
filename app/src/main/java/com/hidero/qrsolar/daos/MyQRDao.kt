package com.hidero.qrsolar.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.hidero.qrsolar.entities.MyQR

@Dao
interface MyQRDao {
    @Insert
    fun insert(qr: MyQR)

    @Update
    fun update(qr: MyQR)

    @Delete
    fun delete(qr: MyQR)

    @Query("DELETE FROM myqr")
    fun deleteAll()

    @Query("SELECT * FROM myqr")
    fun getAll(): LiveData<List<MyQR>>

}