package com.hidero.qrsolar.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData

class CustomViewModel(application: Application) : AndroidViewModel(application) {
    private var text: MutableLiveData<String> = MutableLiveData()

    fun getData(): MutableLiveData<String> {
        return text
    }

    fun setData(data: String) {
        text.value = data
    }

}