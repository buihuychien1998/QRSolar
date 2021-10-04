package com.hidero.qrsolar.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hidero.qrsolar.entities.MyQR
import com.hidero.qrsolar.repositories.MyQRRepository

class MyQRViewModel(application: Application) : AndroidViewModel(application) {
    private var myQR = MutableLiveData<MyQR>()
    private var repository: MyQRRepository? = null
    private var allQRs: LiveData<List<MyQR>>? = null
    fun setMyQR(qr: MyQR) {
        myQR.value = qr
    }

    fun getMyQR(): MutableLiveData<MyQR> {
        return myQR
    }
    

    init {
        repository = MyQRRepository(application)
        allQRs = repository?.getAllMyQRs()
    }

    fun insert(qr: MyQR) {
        repository?.insert(qr)
    }

    fun update(qr: MyQR) {
        repository?.update(qr)
    }

    fun delete(qr: MyQR) {
        repository?.delete(qr)
    }

    fun deleteAll() {
        repository?.deleteAll()
    }

    fun getAll(): LiveData<List<MyQR>> {
        return allQRs!!
    }
}
