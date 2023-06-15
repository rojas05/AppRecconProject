package com.rojasdev.apprecconproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (tableName = "Recolectores",
            foreignKeys = [
                ForeignKey(entity = RecollectionEntity::class, parentColumns = ["Fk_recolector"], childColumns = ["PK_ID_Recolector"])
            ]
    )
data class RecolectoresEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PK_ID_Recolector") val id: Int?,
    @ColumnInfo(name = "name_recolector") val name: String?
)