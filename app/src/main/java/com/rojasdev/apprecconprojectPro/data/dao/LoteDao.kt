package com.rojasdev.apprecconprojectPro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity

@Dao
interface LoteDao {

    @Insert
    suspend fun add(lote : LoteEntity)
}