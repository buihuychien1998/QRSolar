package com.hidero.qrsolar.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import android.graphics.Bitmap

class PhotoEditorViewModel : ViewModel() {
    private var bitmap =  MutableLiveData<Bitmap>()

    fun setBitmap(bm: Bitmap){
        bitmap.value = bm
    }

    fun getBitmap(): MutableLiveData<Bitmap> {
        return bitmap
    }
}
