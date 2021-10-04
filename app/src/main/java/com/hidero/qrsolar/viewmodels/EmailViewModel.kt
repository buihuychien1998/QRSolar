package com.hidero.qrsolar.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.vision.barcode.Barcode
import com.hidero.qrsolar.entities.MyQR

class EmailViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private var email: MutableLiveData<MyQR> = MutableLiveData()
    private var text: MutableLiveData<String> = MutableLiveData()

    fun getValue(): MutableLiveData<MyQR>{
        return email
    }

    fun setValue(data: MyQR?) {
        email.value = data
    }

    fun getData(): MutableLiveData<String>{
        return text
    }

    fun setData(data: String) {
        text.value = data
    }

}
