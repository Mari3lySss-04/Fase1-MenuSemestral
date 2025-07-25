package com.example.menusemestral


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Crea la instancia única de Retrofit que se usa en toda la app.
// Define la base URL para las peticiones al backend.


object Constantes {
    const val BASE_IP = "10.96.37.192" //cambien aca su ip, recuerden que cambia con el wifi
    const val BASE_URL = "http://$BASE_IP/MenuSemestral/MenuSemestral/"
}

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
// ---
//
// Configura y provee una instancia singleton de Retrofit para usar la API.
// Usa ApiService para definir los métodos HTTP.
// ---
