package com.hidero.qrsolar.entities

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.widget.ImageView

data class CreateMenu(
    var id: ObservableField<Int>,
    var title: ObservableField<String>,
    var image: ObservableField<Int>
) {
    companion object {
        @BindingAdapter("android:src")
        @JvmStatic
        fun setImage(iv: ImageView, img: Int) {
            iv.setImageResource(img)
        }
    }
}