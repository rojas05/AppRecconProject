package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.entities.ReportHistoryEntity

@Dao
interface ReportHistoryDao {

    @Insert
    suspend fun add(infoReport: ReportHistoryEntity)

    @Query("SELECT * FROM Historial_informes")
    suspend fun showAll(): List<ReportHistoryEntity>

}