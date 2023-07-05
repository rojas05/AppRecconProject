package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.SettingEntity

@Dao
interface RecolectoresDao {

    @Insert
    suspend fun add(recolector : RecolectoresEntity)

}