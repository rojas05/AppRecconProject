package com.rojasdev.apprecconproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Recolectores")
data class RecolectoresEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Recolector") var id: Int?,
    @ColumnInfo(name = "name_recolector") var name: String,
    @ColumnInfo(name = "estado_recolector") var state: String
)