package com.rojasdev.apprecconproject.data.dao

import androidx.room.Dao
import androidx.room.*
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import kotlinx.coroutines.CoroutineScope

@Dao
interface RecolectoresDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add( name: RecolectoresEntity)

}