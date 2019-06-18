package com.hidero.qrsolar.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.hidero.qrsolar.daos.HistoryDao
import com.hidero.qrsolar.databases.QRDatabase
import com.hidero.qrsolar.entities.History

class HistoryRepository(application: Application) {
    private var historyDao: HistoryDao? = null
    private var allHistories: LiveData<List<History>>? = null

    init {
        val database = QRDatabase.getInstance(application)
        historyDao = database.historyDao()
        allHistories = historyDao!!.getAll()
    }

    fun insert(history: History) {
        InsertHistoryAsyncTask(historyDao!!).execute(history)
    }

    fun update(history: History) {
        UpdateHistoryAsyncTask(historyDao!!).execute(history)
    }

    fun delete(history: History) {
        DeleteHistoryAsyncTask(historyDao!!).execute(history)
    }

    fun deleteAll() {
        DeleteAllHistoryAsyncTask(historyDao!!).execute()
    }

    fun getAllHistories(): LiveData<List<History>> {
        return allHistories!!
    }

    private class InsertHistoryAsyncTask(private val historyDao: HistoryDao) :
        AsyncTask<History, Void, Void>() {

        override fun doInBackground(vararg params: History): Void? {
            historyDao.insert(params[0])
            return null
        }
    }

    private class UpdateHistoryAsyncTask(private val historyDao: HistoryDao) :
        AsyncTask<History, Void, Void>() {

        override fun doInBackground(vararg params: History): Void? {
            historyDao.update(params[0])
            return null
        }
    }

    private class DeleteHistoryAsyncTask(private val historyDao: HistoryDao) :
        AsyncTask<History, Void, Void>() {

        override fun doInBackground(vararg params: History): Void? {
            historyDao.delete(params[0])
            return null
        }
    }

    private class DeleteAllHistoryAsyncTask(private val historyDao: HistoryDao) :
        AsyncTask<History, Void, Void>() {

        override fun doInBackground(vararg params: History): Void? {
            historyDao.deleteAll()
            return null
        }
    }
}