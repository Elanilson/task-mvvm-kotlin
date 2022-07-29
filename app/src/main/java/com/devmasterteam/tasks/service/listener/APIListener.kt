package com.devmasterteam.tasks.service.listener

interface APIListener<T> {
    fun onSuccess(result: T)
    fun onFailure(mensagem : String)
}