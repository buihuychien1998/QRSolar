package com.hidero.qrsolar.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class CustomViewModel(application: Application) : AndroidViewModel(application) {
    private var text: MutableLiveData<String> = MutableLiveData()

    fun getData(): MutableLiveData<String> {
        return text
    }

    fun setData(data: String) {
        text.value = data
    }

}