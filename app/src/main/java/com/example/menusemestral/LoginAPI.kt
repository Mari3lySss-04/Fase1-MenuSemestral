package com.example.menusemestral

import android.content.Context
import android.os.Bundle
import android.widget.Toast

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory


data class LoginRequest(
    val correo: String,
    val contrasena: String
)
data class LoginResponse(
    val status: String,
    val token: String,
    val expires_in: Long,
    val user_id: Int,
    val nombre: String
)

// loginCliente envía el correo y contraseña al backend.
// Si es exitoso, también llama a obtenerPerfil para guardar el email y teléfono.


fun loginCliente(
    context: Context,
    correo: String,
    contrasena: String,
    onSuccess: (String, String) -> Unit // pasa nombre y token
) {
    val api = RetrofitInstance.api //  aquí se accede al ApiService

    val request = LoginRequest(correo, contrasena)
    api.login(request).enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful && response.body()?.status == "success") {
                val loginResponse = response.body()!!
                val nombre = loginResponse.nombre
                val token = loginResponse.token

                Toast.makeText(context, "Bienvenido $nombre", Toast.LENGTH_SHORT).show()

                // Guarda token y nombre
                SessionManager.saveToken(context, token, nombre)

                // Llamar al perfil
                api.obtenerPerfil("Bearer $token").enqueue(object : Callback<PerfilResponse> {
                    override fun onResponse(call: Call<PerfilResponse>, response: Response<PerfilResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            val perfil = response.body()!!.perfil

                            // Guarda email y teléfono
                            SessionManager.saveEmail(context, perfil.email)
                            SessionManager.saveTelefono(context, perfil.telefono)


                            onSuccess(nombre, token)
                        } else {
                            Toast.makeText(context, "Error al obtener el perfil", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PerfilResponse>, t: Throwable) {
                        Toast.makeText(context, "Fallo conexión perfil: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(context, "Error Login: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
// ---
//
// Lógica del login: realiza la petición a la API para autenticar al usuario,
// y posteriormente obtiene el perfil para guardar datos como email y teléfono.
// ---
