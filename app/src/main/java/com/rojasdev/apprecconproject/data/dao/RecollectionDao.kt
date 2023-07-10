package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.data.entities.SettingEntity

@Dao
interface RecollectionDao {

    @Insert
    suspend fun addRecoleccion(recoleccion:RecollectionEntity)
    @Query("SELECT * FROM Recoleccion  WHERE Fk_recolector like :idCollector AND Estado like 'active'")
    suspend fun getCollectionIdCollector(idCollector: Int): List<RecollectionEntity>

    @Query("SELECT Fk_recolector FROM Recoleccion  WHERE Estado like 'active'")
    suspend fun getfKIdCollectorS(): List<Long>
}