package com.hidero.qrsolar.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.databinding.BindingAdapter
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