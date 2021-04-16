package com.inator.calculator.repository

import androidx.lifecycle.LiveData
import com.inator.calculator.History.History
import com.inator.calculator.History.HistoryDao

class HistoryRepository(private val historyDao: HistoryDao) {
    val allHistory: LiveData<List<History>> = historyDao.getHistory()
    suspend fun insert(history: History) {
        historyDao.insert(history)
    }

    suspend fun delete(history: History) {
        historyDao.delete(history)
    }
}