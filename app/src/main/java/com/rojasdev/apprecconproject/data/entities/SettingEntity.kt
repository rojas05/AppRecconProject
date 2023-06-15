package com.rojasdev.apprecconproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Configuracion",
        foreignKeys = [
            ForeignKey(entity = RecolectoresEntity::class, parentColumns = ["Fk_Configuracion"], childColumns = ["PK_ID_Configuracion"])
        ]
    )
data class SettingEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Configuracion") val Id: Int,
    @ColumnInfo(name = "Alimentacion") val feeding: String,
    @ColumnInfo(name = "Precion") val cost: Int,
    @ColumnInfo(name = "Estado") val status: String
)