package com.inator.calculator.History

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @get:Query("SELECT * FROM history order by date, time")
    val getHistory: List<History>

    @Insert
    fun insert(history: History)

    @Delete
    fun delete(history: History)
}