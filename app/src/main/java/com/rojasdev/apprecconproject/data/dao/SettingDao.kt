package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rojasdev.apprecconproject.data.entities.SettingEntity

@Dao
interface SettingDao {

    @Insert
    fun Insertconfig(config:SettingEntity)

}