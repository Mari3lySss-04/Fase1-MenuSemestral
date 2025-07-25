package com.example.menusemestral

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Usuario(val nombre: String, val telefono: String, val email: String)
data class UsuarioEditado(val nombre: String, val telefono: String, val email: String)
data class PerfilResponse(val success: Boolean, val perfil: Perfil)
data class Perfil(val nombre: String, val telefono: String, val email: String)

@OptIn(ExperimentalMaterial3Api::class)
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
    var showWarning by remember { mutableStateOf(false) }

    val pastelTop = Color(0xFFFFD6A5)
    val pastelBottom = Color(0xFFFFF5BA)
    val navBarChocolate = Color(0xFF5D4037)
    val textDark = Color(0xFF000000)
    val warningColor = Color(0xFFF44336)

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = navBarChocolate) {
                NavigationBarItem(
                    selected = false, onClick = { onNavChange("home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color.White) },
                    label = { Text("Inicio", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false, onClick = { onNavChange("carrito") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.White) },
                    label = { Text("Carrito", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false, onClick = { onNavChange("historial") },
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial", tint = Color.White) },
                    label = { Text("Historial", color = Color.White) }
                )
                NavigationBarItem(
                    selected = true, onClick = { onNavChange("perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White) },
                    label = { Text("Perfil", color = Color.White) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(pastelTop, pastelBottom)))
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Mi Perfil",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            Spacer(Modifier.height(16.dp))

            Icon(
                Icons.Default.Person,
                contentDescription = "Foto",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                tint = textDark
            )

            Spacer(Modifier.height(24.dp))

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textDark,
                unfocusedTextColor = textDark,
                focusedLabelColor = textDark,
                unfocusedLabelColor = textDark
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (showWarning && it.contains("@")) {
                        showWarning = false
                    }
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            AnimatedVisibility(visible = showWarning) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(warningColor, shape = RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        "Ingrese un correo válido",
                        color = textDark,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordActual,
                onValueChange = { passwordActual = it },
                label = { Text("Contraseña actual") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (!email.contains("@")) {
                        showWarning = true
                    } else {
                        showWarning = false
                        onActualizarPerfil(UsuarioEditado(nombre, telefono, email), passwordActual)
                    }
                },
                enabled = passwordActual.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA0E7E5))
            ) {
                Text("Guardar Cambios", color = textDark)
            }

            Spacer(Modifier.height(24.dp))
            Divider(thickness = 1.dp, color = Color.Gray)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = nuevaPassword,
                onValueChange = { nuevaPassword = it },
                label = { Text("Nueva contraseña (opcional)") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )
            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nuevaPassword.isNotEmpty()) {
                        onCambiarPassword(passwordActual, nuevaPassword)
                    }
                },
                enabled = passwordActual.isNotEmpty() && nuevaPassword.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA0E7E5))
            ) {
                Text("Cambiar Contraseña", color = textDark)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)), // ← Color actualizado aquí
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión", color = Color.White)
            }
        }
    }
}
