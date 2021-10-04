package com.hidero.qrsolar.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import com.hidero.qrsolar.daos.MyQRDao
import com.hidero.qrsolar.databases.QRDatabase
import com.hidero.qrsolar.entities.MyQR

class MyQRRepository(application: Application) {
    private var myQRDao: MyQRDao? = null
    private var allMyQRs: LiveData<List<MyQR>>? = null

    init {
        val database = QRDatabase.getInstance(application)
        myQRDao = database.myQRDao()
        allMyQRs = myQRDao!!.getAll()
    }

    fun insert(MyQR: MyQR) {
        InsertMyQRAsyncTask(myQRDao!!).execute(MyQR)
    }

    fun update(MyQR: MyQR) {
        UpdateMyQRAsyncTask(myQRDao!!).execute(MyQR)
    }

    fun delete(MyQR: MyQR) {
        DeleteMyQRAsyncTask(myQRDao!!).execute(MyQR)
    }

    fun deleteAll() {
        DeleteAllMyQRAsyncTask(myQRDao!!).execute()
    }

    fun getAllMyQRs(): LiveData<List<MyQR>> {
        return allMyQRs!!
    }



    private class InsertMyQRAsyncTask(private val myQRDao: MyQRDao) :
        AsyncTask<MyQR, Void, Void>() {

        override fun doInBackground(vararg params: MyQR): Void? {
            myQRDao.insert(params[0])
            return null
        }
    }

    private class UpdateMyQRAsyncTask(private val myQRDao: MyQRDao) :
        AsyncTask<MyQR, Void, Void>() {

        override fun doInBackground(vararg params: MyQR): Void? {
            myQRDao.update(params[0])
            return null
        }
    }

    private class DeleteMyQRAsyncTask(private val myQRDao: MyQRDao) :
        AsyncTask<MyQR, Void, Void>() {

        override fun doInBackground(vararg params: MyQR): Void? {
            myQRDao.delete(params[0])
            return null
        }
    }

    private class DeleteAllMyQRAsyncTask(private val myQRDao: MyQRDao) :
        AsyncTask<MyQR, Void, Void>() {

        override fun doInBackground(vararg params: MyQR): Void? {
            myQRDao.deleteAll()
            return null
        }
    }
}