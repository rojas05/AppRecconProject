package com.rojasdev.apprecconprojectPro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconprojectPro.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconprojectPro.data.dataModel.collectorCollection
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity


@Dao
interface RecolectoresDao {

    @Insert
    suspend fun add(recolector : RecolectoresEntity)

    @Query("SELECT * FROM recolectores WHERE estado_recolector == 'active'")
    suspend fun getAllRecolector(): List<RecolectoresEntity>

    @Query("SELECT PK_ID_Recolector FROM recolectores WHERE estado_recolector == 'active'")
    suspend fun getIDCollectors(): List<Long>

    @Query("Delete from recolectores WHERE PK_ID_Recolector LIKE :id")
    suspend fun deleteCollectorId(id: Int)

    @Query("SELECT r.PK_ID_Recolector ,r.name_recolector, sum(re.Cantidad) " +
            "AS kg_collection, sum(re.Cantidad * c.Precio) AS price_total " +
            "FROM recolectores r " +
            "INNER JOIN recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN configuracion c ON re.Fk_Configuracion = c.PK_ID_Configuracion " +
            "WHERE re.Fk_recolector == :collector AND re.Estado == 'active'")
    suspend fun getCollectorAndCollectionTotal(collector: Int): List<collecionTotalCollector>

    @Query("UPDATE recoleccion SET Cantidad = :total WHERE Fk_recolector = :id")
    suspend fun updateCollection(total:Double, id:Int)

    @Query("UPDATE Recolectores SET  estado_recolector = 'archive' WHERE PK_ID_Recolector = :id")
    suspend fun updateCollectorState(id:Int)

    @Query("SELECT r.PK_ID_Recolector, r.name_recolector, re.PK_ID_Recoleccion, re.Cantidad, (con.Precio * re.Cantidad) AS result, con.Precio,"  +
                   "re.Estado, con.Alimentacion, re.Fecha, re.Fk_Configuracion " +
            "FROM recolectores r " +
            "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
            "WHERE re.Estado == :state AND re.Fk_recolector LIKE :id  ORDER BY re.Fecha DESC")
    suspend fun getCollectorAndCollection(state: String, id: Int): List<collectorCollection>


}