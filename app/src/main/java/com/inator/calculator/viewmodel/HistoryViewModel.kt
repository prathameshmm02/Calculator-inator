package com.inator.calculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.inator.calculator.History.AppDatabase
import com.inator.calculator.History.History
import com.inator.calculator.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository
    val allHistory: LiveData<List<History>>

    init {
        val dao = AppDatabase.getDatabase(application).getHistoryDao()
        repository = HistoryRepository(dao)
        allHistory = repository.allHistory
    }

    fun insertHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(history)
    }

    fun deleteHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(history)
    }

}