package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity


@Dao
interface RecolectoresDao {

    @Insert
    suspend fun add(recolector : RecolectoresEntity)

    @Query("SELECT * FROM recolectores")
    suspend fun getAllRecolector(): List<RecolectoresEntity>

}