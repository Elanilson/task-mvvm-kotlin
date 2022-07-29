package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepositorio( context: Context): BaseRepositorio(context) {
    val remote = RetrofitClient.getService(PriorityService::class.java)
    val database = TaskDatabase.getDatabase(context).priorityDAO()

    companion object{
        private val cache = mutableMapOf<Int,String>()
        fun getDescricao(id: Int) : String{
            return cache[id] ?: ""
        }
        fun setDescricao(id: Int,str : String){
            cache[id] = str
        }
    }

    fun salve(list: List<PriorityModel>){
        database.clear()
        database.save(list)
    }

    fun list(listener : APIListener<List<PriorityModel>>){
        executeCall(remote.list(),listener)
    }

    fun getDescricao(id :Int) : String{
        val cached = PriorityRepositorio.getDescricao(id)
        return if(cached == ""){
            val descricao = database.getDescricao(id)
            PriorityRepositorio.setDescricao(id,descricao)
            descricao
        }else{
            cached
        }


    }

    fun list() : List<PriorityModel>{
        return database.list()
    }

}


