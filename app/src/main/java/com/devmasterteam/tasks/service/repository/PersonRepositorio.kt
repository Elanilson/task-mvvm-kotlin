package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class PersonRepositorio( context: Context) :BaseRepositorio(context) {

   private val remote = RetrofitClient.getService(PersonService::class.java)

    fun login(email: String, password: String, listener : APIListener<PersonModel>) {
        if(!isConnectionInternet()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.login(email,password),listener)
    }

    fun create(nome: String, email: String, password: String, listener : APIListener<PersonModel>) {
        if(!isConnectionInternet()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.create(nome,email,password),listener)
    }




}