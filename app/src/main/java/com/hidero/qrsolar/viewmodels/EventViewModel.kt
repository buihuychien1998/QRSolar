package com.hidero.qrsolar.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hidero.qrsolar.entities.Event

class EventViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private var event = MutableLiveData<Event>()
    fun setValue(value: Event?){
        event.value = value
    }
    fun getValue(): MutableLiveData<Event> {
        return event
    }
}
