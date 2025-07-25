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

// Formulario editable para que el usuario actualice sus datos y contraseña
// Llama funciones que actualizan la información en el servidor




@Composable
fun PerfilScreen(
    usuario: Usuario,
    onLogout: () -> Unit,
    onNavChange: (String) -> Unit,
    onActualizarPerfil: (UsuarioEditado, String) -> Unit,
    onCambiarPassword: (String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(usuario.nombre) }
    var telefono by remember { mutableStateOf(usuario.telefono) }
    var email by remember { mutableStateOf(usuario.email) }

    var passwordActual by remember { mutableStateOf("") }
    var nuevaPassword by remember { mutableStateOf("") }

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

            Icon(Icons.Default.Person, contentDescription = "Foto", modifier = Modifier.size(100.dp).clip(CircleShape))


            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

            // ve contraseña actual
            OutlinedTextField(
                value = passwordActual,
                onValueChange = { passwordActual = it },
                label = { Text("Contraseña actual") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(8.dp))

// Botón para guardar perfil
            Button(
                onClick = {
                    onActualizarPerfil(UsuarioEditado(nombre, telefono, email), passwordActual)
                },
                enabled = passwordActual.isNotEmpty()
            ) {
                Text("Guardar Cambios de Perfil")
            }

            Spacer(Modifier.height(24.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

// Cambio de contraseña
            OutlinedTextField(
                value = nuevaPassword,
                onValueChange = { nuevaPassword = it },
                label = { Text("Nueva contraseña (opcional)") },
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (nuevaPassword.isNotEmpty()) {
                        onCambiarPassword(passwordActual, nuevaPassword)
                    }
                },
                enabled = passwordActual.isNotEmpty() && nuevaPassword.isNotEmpty()
            ) {
                Text("Cambiar Contraseña")
            }

        }
    }
}

data class Usuario(val nombre: String, val telefono: String, val email: String)

data class UsuarioEditado(val nombre: String, val telefono: String, val email: String)

data class PerfilResponse(
    val success: Boolean,
    val perfil: Perfil
)

data class Perfil(
    val nombre: String,
    val telefono: String,
    val email: String
)


// Pantalla de perfil editable que permite modificar nombre, teléfono y email.
// También ofrece opción para cambiar la contraseña del usuario actual.
