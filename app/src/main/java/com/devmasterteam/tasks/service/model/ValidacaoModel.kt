package com.devmasterteam.tasks.service.model

class ValidacaoModel( mensagem : String = "") {
    private var status : Boolean =  true;
    private var validacaoMensagem = ""

    init {
        if(mensagem != ""){
             validacaoMensagem = mensagem
            status = false
        }
    }

    fun status() = status
    fun mensagem() = validacaoMensagem
}