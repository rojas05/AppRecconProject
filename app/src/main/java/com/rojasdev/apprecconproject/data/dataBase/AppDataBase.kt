package com.rojasdev.apprecconproject.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rojasdev.apprecconproject.data.dao.RecolectoresDao
import com.rojasdev.apprecconproject.data.dao.RecollectionDao
import com.rojasdev.apprecconproject.data.dao.SettingDao
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.SettingEntity

@Database(
    entities = [RecolectoresEntity::class,
                RecollectionEntity::class,
                SettingEntity::class
               ],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract val RecolectoresDao: RecolectoresDao
    abstract val RecollectionDao: RecollectionDao
    abstract val SettingDao: SettingDao

    companion object{
        const val DATABASE_NAME = "DB_Reccon"
    }

}