package com.example.menusemestral

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.menusemestral.models.CarritoItem
import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.gson.Gson

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory

// Pantalla de solo lectura para mostrar la información del perfil del usuario
// Contiene botón para cerrar sesión y navegar a edición de perfil


@Composable
fun PerfilEstaticaScreen(
    usuario: Usuario,
    onLogout: () -> Unit,
    onNavChange: (String) -> Unit,
    onEditarPerfil: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false, onClick = { onNavChange("home") }, icon = {
                    Icon(Icons.Default.Home, contentDescription = "Inicio")
                }, label = { Text("Inicio") })
                NavigationBarItem(selected = false, onClick = { onNavChange("carrito") }, icon = {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }, label = { Text("Carrito") })
                NavigationBarItem(selected = false, onClick = { onNavChange("historial") }, icon = {
                    Icon(Icons.Default.History, contentDescription = "Historial")
                }, label = { Text("Historial") })
                NavigationBarItem(selected = true, onClick = { onNavChange("perfil") }, icon = {
                    Icon(Icons.Default.Person, contentDescription = "Perfil")
                }, label = { Text("Perfil") })
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Perfil", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Icon(
                Icons.Default.Person,
                contentDescription = "Foto",
                modifier = Modifier.size(100.dp).clip(CircleShape)
            )
            Spacer(Modifier.height(16.dp))

            Text("Nombre: ${usuario.nombre}")
            Text("Teléfono: ${usuario.telefono}")
            Text("Email: ${usuario.email}")
            Spacer(Modifier.height(16.dp))

            Button(onClick = onEditarPerfil) {
                Text("Editar Perfil")
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)

            ) {
                Text("Cerrar sesión")

            }
        }
    }
}
