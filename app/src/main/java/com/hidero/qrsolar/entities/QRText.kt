package com.hidero.qrsolar.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "qrtext")
data class QRText(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int, @ColumnInfo(
        name = "content"
    ) val content: String
) {
}