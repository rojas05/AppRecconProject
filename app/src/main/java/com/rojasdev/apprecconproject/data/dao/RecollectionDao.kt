package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity

@Dao
interface RecollectionDao {

    @Query("SELECT * FROM Recoleccion")
    fun  getAllRecollection(): List<RecollectionEntity>

}