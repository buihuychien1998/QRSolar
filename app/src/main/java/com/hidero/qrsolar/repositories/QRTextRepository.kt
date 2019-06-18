package com.hidero.qrsolar.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.provider.ContactsContract
import com.hidero.qrsolar.daos.QRTextDao
import com.hidero.qrsolar.databases.QRDatabase
import com.hidero.qrsolar.entities.QRText

class QRTextRepository(application: Application) {
    private var qrTextDao: QRTextDao? = null
    private var allQRTexts: LiveData<List<QRText>>? = null

    init {
        val database = QRDatabase.getInstance(application)
        qrTextDao = database.qrTextDao()
        allQRTexts = qrTextDao!!.getAll()
    }

    fun insert(QRText: QRText) {
        InsertQRTextAsyncTask(qrTextDao!!).execute(QRText)
    }

    fun update(QRText: QRText) {
        UpdateQRTextAsyncTask(qrTextDao!!).execute(QRText)
    }

    fun delete(QRText: QRText) {
        DeleteQRTextAsyncTask(qrTextDao!!).execute(QRText)
    }

    fun deleteAll() {
        DeleteAllQRTextAsyncTask(qrTextDao!!).execute()
    }

    fun getAllQRTexts(): LiveData<List<QRText>> {
        return allQRTexts!!
    }

    private class InsertQRTextAsyncTask (private val qrTextDao: QRTextDao) :
        AsyncTask<QRText, Void, Void>() {

        override fun doInBackground(vararg params: QRText): Void? {
            qrTextDao.insert(params[0])
            return null
        }
    }

    private class UpdateQRTextAsyncTask (private val qrTextDao: QRTextDao) :
        AsyncTask<QRText, Void, Void>() {

        override fun doInBackground(vararg params: QRText): Void? {
            qrTextDao.update(params[0])
            return null
        }
    }

    private class DeleteQRTextAsyncTask (private val qrTextDao: QRTextDao) :
        AsyncTask<QRText, Void, Void>() {

        override fun doInBackground(vararg params: QRText): Void? {
            qrTextDao.delete(params[0])
            return null
        }
    }

    private class DeleteAllQRTextAsyncTask (private val qrTextDao: QRTextDao) :
        AsyncTask<QRText, Void, Void>() {

        override fun doInBackground(vararg params: QRText): Void? {
            qrTextDao.deleteAll()
            return null
        }
    }
}