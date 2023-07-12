package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity


@Dao
interface RecolectoresDao {

    @Insert
    suspend fun add(recolector : RecolectoresEntity)

    @Query("SELECT * FROM recolectores")
    suspend fun getAllRecolector(): List<RecolectoresEntity>

    @Query("Delete from recolectores WHERE PK_ID_Recolector LIKE :id")
    suspend fun deleteCollectorId(id: Int)

    @Query("UPDATE recoleccion SET Cantidad = :total WHERE Fk_recolector = :id")
    suspend fun updateCollection(total:Double, id:Int)

    @Query("SELECT r.PK_ID_Recolector, r.name_recolector, re.Cantidad, con.Precio, re.Estado, con.Alimentacion, re.Fecha " +
            "FROM recolectores r " +
            "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
            "WHERE re.Estado == :state AND re.Fk_recolector LIKE :id  ")
    suspend fun getCollectorAndCollection(state: String, id: Int): List<collectorCollection>

}