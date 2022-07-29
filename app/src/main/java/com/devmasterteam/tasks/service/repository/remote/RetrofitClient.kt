package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    companion object{

        private lateinit var INSTACE :Retrofit
        private  var personkey: String = ""
        private  var token : String = ""

        private fun getRetrofitInstace() : Retrofit{
            val htttpClient = OkHttpClient.Builder()

            htttpClient.addInterceptor(object :Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, token)
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personkey)
                        .build()
                    return chain.proceed(request)
                }

            })

            if(!::INSTACE.isInitialized){
                synchronized(RetrofitClient::class){
                    INSTACE = Retrofit.Builder()
                        .baseUrl("http://devmasterteam.com/CursoAndroidAPI/")
                        .client(htttpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }

            }
        return INSTACE
        }

        fun <T> getService(serviceClass : Class<T>) : T{
            return getRetrofitInstace().create(serviceClass)
        }

        fun addHeaders(token:String, personkey: String){
            this.token = token
            this.personkey = personkey
        }
    }

}