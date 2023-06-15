package com.rojasdev.apprecconproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recoleccion")
data class RecollectionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Recoleccion") val ID: Int,
    @ColumnInfo(name = "Cantidad") val total: Double,
    @ColumnInfo(name = "Fecha") val date: String,
    @ColumnInfo(name = "Estado") val state: String,
    @ColumnInfo(name = "Fk_recolector") val collector:Int,
    @ColumnInfo(name = "Fk_Configuracion") val setting:Int
)