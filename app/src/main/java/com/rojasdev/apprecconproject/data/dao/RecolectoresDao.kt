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

    @Query("UPDATE Recolectores SET name_recolector=:name WHERE PK_ID_Recolector=:ID")
    fun update(name:String, ID:Int)

    @Insert
    fun insert(recolectores: List<RecolectoresEntity>)

    @Query("DELETE FROM Recolectores WHERE PK_ID_Recolector=:id")
    fun delete(id: Int)
}