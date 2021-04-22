package com.inator.calculator.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inator.calculator.repository.AppDatabase
import com.inator.calculator.model.History
import com.inator.calculator.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository
    val allHistory: LiveData<List<History>>

    // Is History Panel Open
    private val mutableIsHistoryOpen = MutableLiveData(false)
    val isHistoryOpen: LiveData<Boolean> get() = mutableIsHistoryOpen

    private val mutableClickedHistory: MutableLiveData<History> = MutableLiveData()
    val clickedHistory: LiveData<History> get() = mutableClickedHistory


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

    fun setHistoryOpen(boolean: Boolean) {
        mutableIsHistoryOpen.postValue(boolean)
    }


    fun setClickedExpression(history: History) {
        Log.i("ffdsfs","fsdf")
        mutableClickedHistory.value = history
    }

}