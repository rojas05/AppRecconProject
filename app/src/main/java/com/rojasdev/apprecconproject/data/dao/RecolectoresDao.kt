package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity

@Dao
interface RecolectoresDao {

    @Query("SELECT * FROM Recolectores")
    fun getAll(): List<RecolectoresEntity>

    @Query("SELECT * FROM Recolectores WHERE PK_ID_Recolector = :id")
    fun getById(id: Int): RecolectoresEntity

    @Update
    fun update(recolectores: RecolectoresEntity)

    @Insert
    fun insert(recolectores: List<RecolectoresEntity>)

    @Delete
    fun delete(recolectores: RecolectoresEntity)
}