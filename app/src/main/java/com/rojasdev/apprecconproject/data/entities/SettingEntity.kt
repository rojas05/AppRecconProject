package com.rojasdev.apprecconproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Configuracion")
data class SettingEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Configuracion") val Id: Int?,
    @ColumnInfo(name = "Alimentacion") val feeding: String,
    @ColumnInfo(name = "Precio") val cost: Int,
    @ColumnInfo(name = "Estado") val status: String,
    @ColumnInfo(name = "Fecha") val date: String
)