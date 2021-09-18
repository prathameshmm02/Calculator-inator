package com.inator.calculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inator.calculator.model.History
import com.inator.calculator.repository.AppDatabase
import com.inator.calculator.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository
    val allHistory: LiveData<List<History>>

    // Is History Panel Open
    private val mutableIsHistoryOpen = MutableLiveData(false)
    val isHistoryOpen: LiveData<Boolean> = mutableIsHistoryOpen

    private val mutableClickedHistory: MutableLiveData<History> = MutableLiveData()
    val clickedHistory: LiveData<History> = mutableClickedHistory


    init {
        val dao = AppDatabase.getDatabase(application).getHistoryDao()
        repository = HistoryRepository(dao)
        allHistory = repository.allHistory
    }

    fun insertHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(history)
    }

    fun deleteAllHistory() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun setHistoryOpen(boolean: Boolean) {
        mutableIsHistoryOpen.value = boolean
    }


    fun setClickedExpression(history: History) {
        mutableClickedHistory.value = history
    }

}