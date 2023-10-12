package com.rojasdev.apprecconproject.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Historial_informes")
data class ReportHistoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Pk_Id_Historial") var id: Int?,
    @ColumnInfo(name = "Fecha_Generada") var date: String,
    @ColumnInfo(name = "Nombre_Informe") var name: String,
    @ColumnInfo(name = "Tipo_Informe") var reportType: String,
)
