package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidacaoModel
import com.devmasterteam.tasks.service.repository.PriorityRepositorio
import com.devmasterteam.tasks.service.repository.TaskRepositorio

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepositorio  = TaskRepositorio(application.applicationContext)
    private val priorityRepositorio =PriorityRepositorio(application.applicationContext)

    private var taskFilter = 0

    private var _Tasks = MutableLiveData<List<TaskModel>>()
    var tasks : LiveData<List<TaskModel>> = _Tasks

    private var _delete = MutableLiveData<ValidacaoModel>()
    var delete : LiveData<ValidacaoModel> = _delete

    private var _status = MutableLiveData<ValidacaoModel>()
    var status : LiveData<ValidacaoModel> = _status

    fun list(filter : Int){
        taskFilter = filter
        val listener = object  : APIListener<List<TaskModel>>{
            override fun onSuccess(result: List<TaskModel>) {
                result.forEach{
                    it.priorityDescription = priorityRepositorio.getDescricao(it.priorityId)
                }
                _Tasks.value = result
            }

            override fun onFailure(mensagem: String) {
                //mensagem
            }

        }
        if(filter == TaskConstants.FILTER.ALL){
            taskRepositorio.list(listener)
        }else if(filter == TaskConstants.FILTER.NEXT){
            taskRepositorio.listNext(listener)
        }else{
            taskRepositorio.listOverdure(listener)
        }

    }

    fun delete(id: Int) {
        taskRepositorio.delete(id, object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(mensagem: String) {
                _delete.value = ValidacaoModel(mensagem)
            }

        })

    }

    fun status(id: Int, complete: Boolean) {
        val listener =  object : APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(mensagem: String) {
                _status.value = ValidacaoModel(mensagem)
            }

        }
        if(complete){
            taskRepositorio.complete(id,listener)
        }else{
            taskRepositorio.undo(id, listener)
        }

    }



}