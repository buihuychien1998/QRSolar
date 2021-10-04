package com.hidero.qrsolar.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hidero.qrsolar.entities.QRText
import com.hidero.qrsolar.repositories.QRTextRepository

class TextViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private var repository: QRTextRepository? = null
    private var allQRTexts: LiveData<List<QRText>>? = null
    private var text: MutableLiveData<String> = MutableLiveData()
    private var qrText = MutableLiveData<QRText>()
    fun setValue(value: QRText?){
        qrText!!.value = value
    }

    fun getValue(): MutableLiveData<QRText>{
        return qrText!!
    }

    fun getData(): MutableLiveData<String> {
        return text
    }

    fun setData(data: String) {
        text.value = data
    }

    init {
        repository = QRTextRepository(application)
        allQRTexts = repository?.getAllQRTexts()
    }

    fun insert(text: QRText) {
        repository?.insert(text)
    }

    fun update(text: QRText) {
        repository?.update(text)
    }

    fun delete(text: QRText) {
        repository?.delete(text)
    }

    fun deleteAll() {
        repository?.deleteAll()
    }

    fun getAll(): LiveData<List<QRText>> {
        return allQRTexts!!
    }

}
