package com.rojasdev.apprecconproject.data.dataModel

data class monthPdf(
    val PK_ID_Recolector: Int,
    val name_recolector : String,
    val Estado: String,
    val Alimentacion: String,
    val Fecha: String?,
    val Fk_Configuracion: Int,
    val result: Double,
    val total: Double
)