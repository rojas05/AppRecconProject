package com.rojasdev.apprecconprojectPro.data.dataModel

data class collectorCollection(
    val PK_ID_Recolector: Int,
    val name_recolector : String,
    val PK_ID_Recoleccion: Int,
    val Cantidad : Double,
    val result: Double,
    val Precio: Double,
    val Estado: String,
    val Alimentacion: String,
    val Fecha: String,
    val Fk_Configuracion: Int
)
