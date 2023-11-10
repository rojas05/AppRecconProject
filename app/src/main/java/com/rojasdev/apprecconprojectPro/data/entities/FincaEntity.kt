package com.rojasdev.apprecconprojectPro.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Finca")
class FincaEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Finca") var id: Int?,
    @ColumnInfo(name = "name_finca") var name: String,
    @ColumnInfo(name = "estado_contrasena") var password: String,
    @ColumnInfo(name = "estado_telefono") var cell: Int
)