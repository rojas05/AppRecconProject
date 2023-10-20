package com.rojasdev.apprecconproject.data.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
                SettingEntity::class,
               ],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun RecolectoresDao(): RecolectoresDao
    abstract fun RecollectionDao(): RecollectionDao
    abstract fun SettingDao(): SettingDao

    companion object{
        private const val DATABASE_NAME: String = "DB_Reccon"

        private var Instance:AppDataBase? = null

            fun getInstance(context: Context): AppDataBase {
                if (Instance == null) {
                 Instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, DATABASE_NAME).build()
                }
                   return Instance!!
            }

    }

}