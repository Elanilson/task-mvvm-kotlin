package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidacaoModel
import com.devmasterteam.tasks.service.repository.PersonRepositorio
import com.devmasterteam.tasks.service.repository.PriorityRepositorio
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val personRepositorio = PersonRepositorio(application.applicationContext)
    private val priorityRepositorio = PriorityRepositorio(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private var _Login = MutableLiveData<ValidacaoModel>()
    var login : LiveData<ValidacaoModel> = _Login

    private var _LoggedUser = MutableLiveData<Boolean>()
    var loggerDUser : LiveData<Boolean> = _LoggedUser



    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepositorio.login(email,password, object  : APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {

                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY,result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY,result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME,result.name)

                RetrofitClient.addHeaders(result.token,result.personKey)

                _Login.value = ValidacaoModel()
            }

            override fun onFailure(mensagem: String) {
                _Login.value  = ValidacaoModel(mensagem)
            }

        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
      val token =  securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
      val personKey =  securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeaders(token,personKey)
        val logger = (token != "" && personKey != "")

        _LoggedUser.value = logger

        if(!logger){
            priorityRepositorio.list(object : APIListener<List<PriorityModel>>{
                override fun onSuccess(result: List<PriorityModel>) {
                   priorityRepositorio.salve(result)
                }

                override fun onFailure(mensagem: String) {
                    val s = ""
                }

            })
        }
    }

}