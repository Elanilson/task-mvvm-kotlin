package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidacaoModel
import com.devmasterteam.tasks.service.repository.PriorityRepositorio
import com.devmasterteam.tasks.service.repository.TaskRepositorio

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {
    private val priorityRepositorio = PriorityRepositorio(application.applicationContext)
    private val taskRepositorio = TaskRepositorio(application.applicationContext)

    private var _Prioridades = MutableLiveData<List<PriorityModel>>()
    var prioridade : LiveData<List<PriorityModel>> = _Prioridades

    private var _TaskSave = MutableLiveData<ValidacaoModel>()
    var taskSave : LiveData<ValidacaoModel> = _TaskSave

    private var _Task = MutableLiveData<TaskModel>()
    var task : LiveData<TaskModel> = _Task

    private var _TaskLoad = MutableLiveData<ValidacaoModel>()
    var taskLoad : LiveData<ValidacaoModel> = _TaskLoad

    fun loadPrioridades() {
        _Prioridades.value  = priorityRepositorio.list()

    }

    fun load(id: Int ){
        taskRepositorio.load(id, object : APIListener<TaskModel>{
            override fun onSuccess(result: TaskModel) {
                _Task.value = result
            }
            override fun onFailure(mensagem: String) {
                _TaskLoad.value = ValidacaoModel(mensagem)
            }

        })
    }

    fun save(task: TaskModel) {
        val  listener = object :APIListener<Boolean>{
            override fun onSuccess(result: Boolean) {
                _TaskSave.value = ValidacaoModel()
            }

            override fun onFailure(mensagem: String) {
                _TaskSave.value = ValidacaoModel(mensagem)
            }

        }

        if(task.id == 0){
            taskRepositorio.save(task,listener)
        }else{
            taskRepositorio.update(task, listener)

        }


    }

}