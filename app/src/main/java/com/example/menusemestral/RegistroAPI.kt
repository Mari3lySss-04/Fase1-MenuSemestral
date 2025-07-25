package com.example.menusemestral

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory



// Punción para registrar usuario con Retrofit
data class RegistroRequest(val nombre: String, val telefono: String, val correo: String, val contrasena: String)
data class RegistroResponse(val status: String, val message: String)



// Esta función ejecuta el registro de un nuevo usuario al backend mediante una llamada Retrofit.
// Muestra un Toast con el resultado y ejecuta un callback en caso de éxito.


fun registrarUsuario(
    context: Context,
    nombre: String,
    telefono: String,
    correo: String,
    contrasena: String,
    onSuccess: () -> Unit
) {
    val api = RetrofitInstance.api


    val request = RegistroRequest(nombre, telefono, correo, contrasena)
    api.registrar(request).enqueue(object : Callback<RegistroResponse> {
        override fun onResponse(
            call: Call<RegistroResponse>,
            response: Response<RegistroResponse>
        ) {
            if (response.isSuccessful && response.body()?.status == "success") {
                Toast.makeText(context, " ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(
                    context,
                    " ${response.body()?.message ?: "Error en el registro"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<RegistroResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

/*
*
* Archivo que contiene la lógica de red para registrar un nuevo usuario.
* Usa Retrofit y se conecta con `registro.php` en el backend. En caso de éxito,
* se ejecuta un callback `onSuccess` y se muestra un Toast al usuario.
*
* Se llama desde RegistroScreen.kt al presionar el botón de "Registrarse".
*/
