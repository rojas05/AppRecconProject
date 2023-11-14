package com.rojasdev.apprecconprojectPro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity

@Dao
interface LoteDao {

    @Insert
    suspend fun add( lote : LoteEntity)
}