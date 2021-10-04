package com.hidero.qrsolar.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hidero.qrsolar.entities.History
import com.hidero.qrsolar.repositories.HistoryRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private var history = MutableLiveData<History>()
    private var repository: HistoryRepository? = null
    private var allHistories: LiveData<List<History>>? = null
    fun setHistory(history: History) {
        this.history.value = history
    }

    fun getHistory(): MutableLiveData<History> {
        return history
    }


    init {
        repository = HistoryRepository(application)
        allHistories = repository?.getAllHistories()
    }

    fun insert(history: History) {
        repository?.insert(history)
    }

    fun update(history: History) {
        repository?.update(history)
    }

    fun delete(history: History) {
        repository?.delete(history)
    }

    fun deleteAll() {
        repository?.deleteAll()
    }

    fun getAll(): LiveData<List<History>> {
        return allHistories!!
    }
}
