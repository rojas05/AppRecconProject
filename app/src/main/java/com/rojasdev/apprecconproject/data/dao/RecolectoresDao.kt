package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rojasdev.apprecconproject.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.data.dataModel.monthPdf
import com.rojasdev.apprecconproject.data.dataModel.totalMonthPdf
import com.rojasdev.apprecconproject.data.dataModel.weekPdf
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity

@Dao
interface RecolectoresDao {

    @Insert
    suspend fun add(recolector : RecolectoresEntity)

    @Query("UPDATE Recolectores SET  estado_recolector = 'archive' WHERE PK_ID_Recolector = :id")
    suspend fun updateCollectorState(id:Int)

    @Query("UPDATE Recolectores SET  name_recolector = :name WHERE PK_ID_Recolector = :id")
    suspend fun updateCollectorName(id:Int, name:String)

    @Query("Delete FROM recolectores WHERE PK_ID_Recolector LIKE :id")
    suspend fun deleteCollectorId(id: Int)

    @Query("SELECT * FROM recolectores WHERE estado_recolector == 'active'")
    suspend fun getAllRecolector(): List<RecolectoresEntity>

    @Query("SELECT PK_ID_Recolector FROM recolectores WHERE estado_recolector == 'active'")
    suspend fun getIDCollectors(): List<Long>

    @Query("SELECT PK_ID_Recolector FROM recolectores")
    suspend fun getAll(): List<Long>

    @Query("SELECT r.PK_ID_Recolector ,r.name_recolector, sum(re.Cantidad) " +
            "AS kg_collection, sum(re.Cantidad * c.Precio) AS price_total " +
            "FROM recolectores r " +
            "INNER JOIN recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN configuracion c ON re.Fk_Configuracion = c.PK_ID_Configuracion " +
            "WHERE re.Fk_recolector == :collector AND re.Estado == 'active'")
    suspend fun getCollectorAndCollectionTotal(collector: Int): List<collecionTotalCollector>

    @Query("SELECT r.PK_ID_Recolector, r.name_recolector, re.PK_ID_Recoleccion, re.Cantidad, " +
            "con.Precio * re.Cantidad AS result, con.Precio, "  +
            "re.Estado, con.Alimentacion, re.Fecha, re.Fk_Configuracion " +
            "FROM recolectores r " +
            "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
            "WHERE re.Estado == :state AND re.Fk_recolector LIKE :id  ORDER BY re.Fecha DESC")
    suspend fun getCollectorAndCollection(state: String, id: Int): List<collectorCollection>

    @Query("SELECT r.PK_ID_Recolector, r.name_recolector, re.PK_ID_Recoleccion, SUM(re.Cantidad) AS Cantidad, " +
            "SUM(re.Cantidad * con.Precio) AS result, con.Precio, " +
            "re.Estado, con.Alimentacion, re.Fecha, re.Fk_Configuracion " +
            "FROM recolectores r " +
            "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
            "WHERE re.Fecha LIKE :dates AND r.PK_ID_Recolector == :id " +
            "ORDER BY re.Fecha DESC")
    suspend fun getAllCollectorAndCollectionId(dates: String, id: Int): List<allCollecionAndCollector>

    @Query("SELECT r.PK_ID_Recolector, r.name_recolector, re.PK_ID_Recoleccion, sum(re.Cantidad) AS Cantidad, " +
            "sum(re.Cantidad * con.Precio) AS result, con.Precio, " +
            "re.Estado, con.Alimentacion, re.Fecha,  re.Fk_Configuracion " +
            "FROM recolectores r " +
            "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
            "WHERE re.Fecha LIKE :dates ORDER BY re.Fecha DESC")
    suspend fun getTotalKgDate(dates: String): List<allCollecionAndCollector>

    @Query("SELECT r.PK_ID_Recolector, r.name_recolector, " +
            "sum(re.Cantidad) AS result, " +
            "sum(con.Precio * re.Cantidad) AS total, "  +
            "re.Estado, con.Alimentacion, re.Fecha, re.Fk_Configuracion " +
            "FROM recolectores r " +
            "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
            "WHERE re.Fecha >= :startDate AND re.Fecha <= :endDate AND con.Alimentacion LIKE :aliment " +
            "GROUP BY re.Fk_recolector, con.Alimentacion")
    suspend fun getWeekPdf(startDate:String,endDate:String,aliment:String): List<weekPdf>

    @Query("SELECT r.PK_ID_Recolector, r.name_recolector, re.Estado, con.Alimentacion, re.Fecha, re.Fk_Configuracion, " +
            "SUM(re.Cantidad) AS result, " +
            "SUM(con.Precio * re.Cantidad) AS total "  +
            "FROM recolectores r " +
            "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
            "WHERE re.Fecha LIKE :date AND con.Alimentacion LIKE :aliment " +
            "GROUP BY re.Fk_recolector, con.Alimentacion")
    suspend fun getPdfInfo(date: String, aliment: String): List<monthPdf>

    @Query("SELECT con.Precio, " +
            "SUM(re.Cantidad) AS result, " +
            "SUM(re.Cantidad * con.Precio) AS total "  +
            "FROM recolectores r " +
            "INNER JOIN Recoleccion re ON r.PK_ID_Recolector = re.Fk_recolector " +
            "INNER JOIN Configuracion con ON re.Fk_Configuracion = con.PK_ID_Configuracion " +
            "WHERE re.Fecha LIKE :date AND con.Alimentacion LIKE :aliment ")
    suspend fun getTotalPdf(date: String, aliment: String): List<totalMonthPdf>

}