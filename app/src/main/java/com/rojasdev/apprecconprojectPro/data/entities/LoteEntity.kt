package com.rojasdev.apprecconprojectPro.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lote")
class LoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Lote") var id: Int?,
    @ColumnInfo(name = "name_lote") var name: String
)