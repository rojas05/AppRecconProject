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
    @Query("SELECT * FROM Recoleccion  WHERE Fk_recolector == :idCollector AND Estado == 'active'")
    suspend fun getCollectionIdCollector(idCollector: Int): List<RecollectionEntity>
}