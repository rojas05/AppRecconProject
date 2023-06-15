package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity

@Dao
interface RecollectionDao {

    @Query("SELECT * FROM Recoleccion")
    suspend fun  getAllRecollection(): List<RecollectionEntity>

    @Query("UPDATE Recoleccion SET Estado=:state, Cantidad=:Cantidad WHERE PK_ID_Recoleccion=:id")
    suspend fun update(state:String, Cantidad:Int, id:Int)

    @Query("DELETE FROM Recoleccion WHERE PK_ID_Recoleccion=:id")
    suspend fun delete(id:Int)

    @Insert
    suspend fun insert(Recoleccion: List<RecollectionEntity>)

}