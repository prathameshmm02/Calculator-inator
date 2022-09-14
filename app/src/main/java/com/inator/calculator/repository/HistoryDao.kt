package com.inator.calculator.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.inator.calculator.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getHistory(): LiveData<List<History>>

    @Insert
    suspend fun insert(history: History)

    @Query("Delete From history")
    suspend fun deleteAll()

    @Query("DELETE FROM history WHERE eid = :id")
    suspend fun delete(id: Int)
}