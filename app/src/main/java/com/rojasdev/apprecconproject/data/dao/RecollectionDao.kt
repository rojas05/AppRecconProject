package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity

@Dao
interface RecollectionDao {

    @Insert
    suspend fun addRecollection(recoleccion:RecollectionEntity)

    @Query("SELECT Fecha FROM Recoleccion")
    suspend fun getDateCollection(): List<String>

    @Query("SELECT Fk_recolector FROM Recoleccion  WHERE Estado == 'active'")
    suspend fun getFkIdCollectors(): List<Long>

    @Query("UPDATE Recoleccion SET Cantidad = :kg, Fecha = :date,  Fk_Configuracion = :feed WHERE PK_ID_Recoleccion = :idCollection AND Fk_recolector = :idCollector")
    suspend fun updateCollection(idCollection:Int, date:String, idCollector:Int, kg:Double, feed:Int)

    @Query("UPDATE recoleccion SET estado = 'archive' WHERE Fk_recolector = :id")
    suspend fun updateCollectionState(id:Int)
}