package com.inator.calculator.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.inator.calculator.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history order by date, time")
    fun getHistory(): LiveData<List<History>>

    @Insert
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)
}