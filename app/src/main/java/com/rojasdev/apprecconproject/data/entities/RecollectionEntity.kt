package com.rojasdev.apprecconproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Recoleccion",
            foreignKeys = [
                ForeignKey(entity = RecolectoresEntity::class, parentColumns = ["PK_ID_Recolector"], childColumns = ["Fk_recolector"]),
                ForeignKey(entity = SettingEntity::class, parentColumns = ["PK_ID_Configuracion"], childColumns = ["Fk_Configuracion"])
            ]
    )
data class RecollectionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Recoleccion") val ID: Int?,
    @ColumnInfo(name = "Cantidad") val total: Double,
    @ColumnInfo(name = "Fecha") val date: String,
    @ColumnInfo(name = "Estado") val state: String,
    @ColumnInfo(name = "Fk_recolector") val collector:Int,
    @ColumnInfo(name = "Fk_Configuracion") val setting:Int
)