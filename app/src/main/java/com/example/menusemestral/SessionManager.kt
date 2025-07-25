
package com.example.menusemestral

import android.content.Context
import androidx.core.content.edit

/**
 * Guarda y elimina el token en SharedPreferences.
 */

// Guarda y recupera datos de sesión como token, nombre, correo y teléfono del usuario.

object SessionManager {

    private const val PREF_NAME = "MiAppPrefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_NOMBRE = "nombre"
    private const val KEY_EMAIL = "email"
    private const val KEY_TELEFONO = "telefono"

    fun saveToken(context: Context, token: String, nombre: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_NOMBRE, nombre)
            .apply()
    }

    // ete guarda email y teléfono
    fun saveEmailAndTelefono(context: Context, email: String, telefono: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_EMAIL, email)
            putString(KEY_TELEFONO, telefono)
        }
    }

    fun fetchToken(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, "") ?: ""
    }

    fun clearToken(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            remove(KEY_TOKEN)
            remove(KEY_NOMBRE)
            remove(KEY_EMAIL)
            remove(KEY_TELEFONO)
            apply()
        }
    }



    fun fetchNombre(context: Context): String {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(KEY_NOMBRE, "Usuario") ?: "Usuario"
    }

    fun saveTelefono(context: Context, telefono: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TELEFONO, telefono).apply()
    }

    fun saveEmail(context: Context, email: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_EMAIL, email).apply()
    }

    fun fetchTelefono(context: Context): String {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(KEY_TELEFONO, "") ?: ""
    }

    fun fetchEmail(context: Context): String {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(KEY_EMAIL, "") ?: ""
    }



}
// ---
//
// Maneja el almacenamiento local de la sesión del usuario utilizando SharedPreferences.
// Guarda token, nombre, email y teléfono.
// ---
