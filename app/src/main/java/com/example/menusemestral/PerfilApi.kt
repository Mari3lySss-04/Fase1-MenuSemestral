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

// Llama a la API para actualizar el perfil del usuario con token y datos modificados
// Guarda localmente los nuevos datos si el backend responde con éxito


data class GenericResponse(val success: Boolean, val message: String? = null)



fun actualizarPerfil(
    context: Context,
    usuario: UsuarioEditado,
    password: String,
    onSuccess: (Boolean) -> Unit
) {
    val api = RetrofitInstance.api
    val call = api.actualizarPerfil(
        token = "Bearer ${SessionManager.fetchToken(context)}",
        usuario = usuario,
        password = password
    )

    call.enqueue(object : Callback<GenericResponse> {
        override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
            if (response.isSuccessful && response.body()?.success == true) {
                // aca tan los cambios en SessionManager
                SessionManager.saveEmailAndTelefono(context, usuario.email, usuario.telefono)
                SessionManager.saveToken(context, SessionManager.fetchToken(context), usuario.nombre)

                Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                onSuccess(true)
            } else {
                Toast.makeText(context, "Error al actualizar perfil", Toast.LENGTH_SHORT).show()
                onSuccess(false)
            }
        }

        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
            Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
            onSuccess(false)
        }
    })
}
fun cambiarPassword(
    context: Context,
    actual: String,
    nueva: String,
    callback: (Boolean) -> Unit
) {
    val token = SessionManager.fetchToken(context)
    if (token.isNullOrEmpty()) {
        Toast.makeText(context, "Token no encontrado", Toast.LENGTH_SHORT).show()
        callback(false)
        return
    }

    val body = mapOf(
        "actual" to actual,
        "nueva" to nueva
    )

    val call = RetrofitInstance.api.cambiarPassword("Bearer $token", body)
    call.enqueue(object : Callback<GenericResponse> {
        override fun onResponse(
            call: Call<GenericResponse>,
            response: Response<GenericResponse>
        ) {
            if (response.isSuccessful && response.body()?.success == true) {
                Toast.makeText(context, "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show()
                callback(true)
            } else {
                Toast.makeText(
                    context,
                    "Error: ${response.body()?.message ?: "No se pudo cambiar"}",
                    Toast.LENGTH_SHORT
                ).show()
                callback(false)
            }
        }

        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
            Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            callback(false)
        }
    })
}
// Este archivo define funciones de red para actualizar el perfil y cambiar contraseña.
// Usa Retrofit para comunicarse con la API y SessionManager para persistir cambios localmente.


