package com.hidero.qrsolar.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.graphics.Bitmap
import java.nio.ByteBuffer

@Entity(tableName = "myqr")
data class MyQR(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray,
    @ColumnInfo(name = "species")
    var species: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyQR

        if (id != other.id) return false
        if (title != other.title) return false
        if (!image.contentEquals(other.image)) return false
        if (species != other.species) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + species.hashCode()
        return result
    }
//    var data = ObservableField<MyQR>()

}