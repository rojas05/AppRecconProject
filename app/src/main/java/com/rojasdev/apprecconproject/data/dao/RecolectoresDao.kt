package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity


@Dao
interface RecolectoresDao {

    @Insert
    suspend fun add(recolector : RecolectoresEntity)

    @Query("SELECT * FROM recolectores")
    suspend fun getAllRecolector(): List<RecolectoresEntity>

    @Query("delete from recolectores WHERE PK_ID_Recolector LIKE :id")
    suspend fun deleteCollectorId(id: Int)

    @Query("SELECT r.PK_ID_Recolector ,r.name_recolector, sum(re.Cantidad) " +
            "as kg_collection, sum(re.Cantidad * c.Precio) as price_total " +
            "FROM recolectores r " +
            "INNER JOIN recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN configuracion c ON re.Fk_Configuracion = c.PK_ID_Configuracion " +
            "WHERE re.Fk_recolector == :collector")
    suspend fun getCollectorAndCollectionTotal(collector: Int): List<collecionTotalCollector>

}