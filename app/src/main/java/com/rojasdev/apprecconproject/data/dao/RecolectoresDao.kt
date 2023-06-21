package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity

@Dao
interface RecolectoresDao {

    @Insert
    suspend fun add(vararg recolecctor : RecolectoresEntity):List<Long>

}