package com.hidero.qrsolar.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qrtext")
data class QRText(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int, @ColumnInfo(
        name = "content"
    ) val content: String
) {
}