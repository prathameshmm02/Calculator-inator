package com.inator.calculator.History

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history order by date, time")
    fun getHistory(): LiveData<List<History>>

    @Insert
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)
}