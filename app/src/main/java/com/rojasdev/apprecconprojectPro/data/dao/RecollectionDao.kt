package com.rojasdev.apprecconprojectPro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconprojectPro.data.entities.RecollectionEntity

@Dao
interface RecollectionDao {

    @Insert
    suspend fun addRecoleccion(recoleccion:RecollectionEntity)

    @Query("SELECT * FROM Recoleccion  WHERE Fk_recolector like :idCollector AND Estado = 'active'")
    suspend fun getCollectionIdCollector(idCollector: Int): List<RecollectionEntity>

    @Query("SELECT Fk_recolector FROM Recoleccion  WHERE Estado == 'active'")
    suspend fun getfKIdCollectors(): List<Long>

    @Query("UPDATE Recoleccion SET Cantidad = :Kg, Fk_Configuracion = :feed WHERE PK_ID_Recoleccion = :idCollecion AND Fk_recolector = :idCollector")
    suspend fun updateCollection(idCollecion:Int, idCollector:Int, Kg:Double, feed:Int)

    @Query("UPDATE recoleccion SET estado = 'archive' WHERE Fk_recolector = :id")
    suspend fun updateCollectionState(id:Int)
}