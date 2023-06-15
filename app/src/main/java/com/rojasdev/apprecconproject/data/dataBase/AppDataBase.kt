package com.rojasdev.apprecconproject.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rojasdev.apprecconproject.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.data.dao.RecollectionDao
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity

@Database(
    entities = [RecolectoresEntity::class, RecollectionEntity::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract val RecolectoresDao: RecolectoresDao
    abstract val RecollectionDao: RecollectionDao

    companion object{
        const val DATABASE_NAME = "DB_Reccon"
    }

}