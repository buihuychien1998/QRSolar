package com.hidero.qrsolar.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.hidero.qrsolar.entities.History

@Dao
interface HistoryDao {
    @Insert
    fun insert(qr: History)

    @Update
    fun update(qr: History)

    @Delete
    fun delete(qr: History)

    @Query("DELETE FROM history")
    fun deleteAll()

    @Query("SELECT * FROM history")
    fun getAll(): LiveData<List<History>>

}