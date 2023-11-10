package com.rojasdev.apprecconprojectPro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.rojasdev.apprecconprojectPro.data.entities.FincaEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity

@Dao
interface FincaDao {

    @Insert
    suspend fun add(finca : FincaEntity)

}