package com.rojasdev.apprecconproject.data.dataModel

data class allCollecionAndCollector (
    val PK_ID_Recolector: Int,
    val name_recolector : String,
    val PK_ID_Recoleccion: Int,
    val Cantidad : Double,
    val Estado: String,
    val Alimentacion: String,
    val Fecha: String,
    val Hora: String,
    val Fk_Configuracion: Int,
    val Precio: Double,
    val result: Double
)
