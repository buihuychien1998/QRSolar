package com.hidero.qrsolar.daos

import androidx.lifecycle.LiveData
import androidx.room.*
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