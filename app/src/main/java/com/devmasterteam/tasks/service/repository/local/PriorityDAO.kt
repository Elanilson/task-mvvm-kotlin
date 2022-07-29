package com.devmasterteam.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.remote.PriorityService

@Dao
interface PriorityDAO {

    @Insert
    fun save(list: List<PriorityModel>)

    @Query("select * from priorit")
    fun list() : List<PriorityModel>

    @Query("select description from priorit where id = :id")
    fun getDescricao(id : Int) : String

    @Query("delete from priorit")
    fun clear()
}