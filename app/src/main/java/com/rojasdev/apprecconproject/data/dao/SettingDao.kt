package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.dataModel.totalWeekPdf
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.data.entities.SettingEntity

@Dao
interface SettingDao {

    @Insert
    suspend fun Insertconfig(config:SettingEntity)

    @Query("SELECT * FROM configuracion WHERE Estado == 'active' AND Alimentacion == :aliment")
    suspend fun getAliment(aliment: String): List<SettingEntity>

    @Query("SELECT * FROM configuracion WHERE Estado == 'archived' ORDER BY PK_ID_Configuracion DESC")
    suspend fun getAlimentArchived(): List<SettingEntity>

    @Query("SELECT * FROM configuracion WHERE Estado == :state ")
    suspend fun getAlimentState(state : String): List<SettingEntity>

    @Query("UPDATE configuracion SET Estado = :status WHERE PK_ID_Configuracion == :id")
    suspend fun UpdateConfig(id: Int?, status: String)

    @Query("SELECT con.Precio, sum(re.Cantidad) as cantidad, sum(re.Cantidad * con.Precio) as total " +
            "FROM configuracion con  " +
            "INNER JOIN Recoleccion re ON con.PK_ID_Configuracion = re.Fk_Configuracion " +
            "WHERE re.Fecha >= :startDate AND re.Fecha <= :endDate AND con.Alimentacion LIKE :aliment "+
            "GROUP BY re.Fk_recolector, con.Alimentacion")
    suspend fun getTotalPdfWeek(startDate:String,endDate:String,aliment:String): List<totalWeekPdf>

    @Query("SELECT con.Precio, sum(re.Cantidad) as cantidad, sum(re.Cantidad * con.Precio) as total " +
            "FROM configuracion con  " +
            "INNER JOIN Recoleccion re ON con.PK_ID_Configuracion = re.Fk_Configuracion " +
            "WHERE re.Estado == 'active'")
    suspend fun getTotalCollectionActive(): List<totalWeekPdf>
}