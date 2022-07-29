package com.devmasterteam.tasks.service.model

import com.google.gson.annotations.SerializedName

/*

{
"token": "d+CVvGcmQ+BGHK24T2S9HT7huf33Y8Z630phkYL77F8=",
"personKey": "AOanaFzaY+kqXNN56MsmjatpH5y6yrqq1",
"name": "user_name"
}

 */
class PersonModel {

    @SerializedName("token")
   lateinit var token : String

   @SerializedName("personKey")
   lateinit var personKey : String

   @SerializedName("name")
   lateinit var name : String
}