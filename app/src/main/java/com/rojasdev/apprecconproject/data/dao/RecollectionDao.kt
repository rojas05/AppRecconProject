package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity

@Dao
interface RecollectionDao {

    @Insert
    fun addRecoleccion(recoleccion:RecollectionEntity)

}