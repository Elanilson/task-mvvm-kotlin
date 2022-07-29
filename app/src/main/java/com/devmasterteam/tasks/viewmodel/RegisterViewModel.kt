package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidacaoModel
import com.devmasterteam.tasks.service.repository.PersonRepositorio
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = PersonRepositorio(application.applicationContext)
    private  val securityPreferences = SecurityPreferences(application.applicationContext)

    private var _usuario = MutableLiveData<ValidacaoModel>()
    var usuario : LiveData<ValidacaoModel> = _usuario

    fun create(name: String, email: String, password: String) {
        repositorio.create(name,email,password, object : APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY,result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY,result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME,result.name)

                RetrofitClient.addHeaders(result.token,result.personKey)
            }

            override fun onFailure(mensagem: String) {
                _usuario.value = ValidacaoModel(mensagem)
            }

        })

    }

}