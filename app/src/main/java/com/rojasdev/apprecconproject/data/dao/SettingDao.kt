package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.SettingEntity

@Dao
interface SettingDao {

    @Insert
    suspend fun Insertconfig(config:SettingEntity)

    @Query("SELECT * FROM configuracion WHERE Estado == 'active' AND Alimentacion == :aliment")
    suspend fun getAliment(aliment: String): List<SettingEntity>

    @Query("UPDATE configuracion SET Estado = :status WHERE PK_ID_Configuracion == :id")
    suspend fun UpdateConfig(id: Int?, status: String)

    @Query("UPDATE configuracion SET Alimentacion = :feending WHERE PK_ID_Configuracion == :id")
    suspend fun updateFeending(feending:String, id: Int? )

}