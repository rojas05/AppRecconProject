package com.rojasdev.apprecconprojectPro.data.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rojasdev.apprecconprojectPro.data.dao.FincaDao
import com.rojasdev.apprecconprojectPro.data.dao.LoteDao
import com.rojasdev.apprecconprojectPro.data.dao.RecolectoresDao
import com.rojasdev.apprecconprojectPro.data.dao.RecollectionDao
import com.rojasdev.apprecconprojectPro.data.dao.SettingDao
import com.rojasdev.apprecconprojectPro.data.entities.FincaEntity
import com.rojasdev.apprecconprojectPro.data.entities.LoteEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecollectionEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.data.entities.SettingEntity

@Database(
    entities = [RecolectoresEntity::class,
                RecollectionEntity::class,
                SettingEntity::class,
                LoteEntity::class,
                FincaEntity::class
               ],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun RecolectoresDao(): RecolectoresDao
    abstract fun RecollectionDao(): RecollectionDao
    abstract fun SettingDao(): SettingDao

    abstract fun LoteDao(): LoteDao
    abstract fun FincaDao(): FincaDao

    companion object{
        const val DATABASE_NAME = "DB_Reccon"

        private var Instance:AppDataBase? = null

            fun getInstance(context: Context): AppDataBase {
                if (Instance == null) {
                 Instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, AppDataBase.DATABASE_NAME)
                     .build()
                }
                   return Instance!!
            }

    }

}