package com.hidero.qrsolar.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.databinding.BindingAdapter
import android.widget.ImageView

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "image")
    var image: Int,
    @ColumnInfo(name = "time")
    var time: String
) {
    companion object {
        @BindingAdapter("android:src")
        @JvmStatic
        fun setImage(iv: ImageView, img: Int) {
            iv.setImageResource(img)
        }
    }
}