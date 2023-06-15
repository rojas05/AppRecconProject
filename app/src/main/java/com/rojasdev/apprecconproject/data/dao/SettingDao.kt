package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rojasdev.apprecconproject.data.entities.SettingEntity

@Dao
interface SettingDao {

    @Query("SELECT * FROM Configuracion")
    suspend fun getALL(): List<SettingEntity>

    @Query("UPDATE Configuracion SET Precio=:presio, Alimentacion=:Alimentacion WHERE PK_ID_Configuracion=:id")
    suspend fun update(presio:Int, Alimentacion:String, id:Int)

    @Query("DELETE FROM Configuracion WHERE PK_ID_Configuracion=:settingId")
    suspend fun delete(settingId: Int)

    @Insert
    suspend fun insert(setting: List<SettingEntity>)

}