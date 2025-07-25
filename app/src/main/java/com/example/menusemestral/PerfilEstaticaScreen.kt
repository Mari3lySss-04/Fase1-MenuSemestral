package com.example.menusemestral

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilEstaticaScreen(
    usuario: Usuario,
    onLogout: () -> Unit,
    onNavChange: (String) -> Unit,
    onEditarPerfil: () -> Unit
) {
    // Colores utilizados en otras pantallas
    val pastelTop = Color(0xFFFFD6A5)
    val pastelBottom = Color(0xFFFFF5BA)
    val navBarChocolate = Color(0xFF8D6E63) // Color chocolate usado

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = navBarChocolate) {
                NavigationBarItem(selected = false, onClick = { onNavChange("home") }, icon = {
                    Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color.White)
                }, label = { Text("Inicio", color = Color.White) })
                NavigationBarItem(selected = false, onClick = { onNavChange("carrito") }, icon = {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.White)
                }, label = { Text("Carrito", color = Color.White) })
                NavigationBarItem(selected = false, onClick = { onNavChange("historial") }, icon = {
                    Icon(Icons.Default.History, contentDescription = "Historial", tint = Color.White)
                }, label = { Text("Historial", color = Color.White) })
                NavigationBarItem(selected = true, onClick = { onNavChange("perfil") }, icon = {
                    Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
                }, label = { Text("Perfil", color = Color.White) })
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(colors = listOf(pastelTop, pastelBottom)))
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Perfil", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF333333))
            Spacer(Modifier.height(16.dp))

            Icon(
                Icons.Default.Person,
                contentDescription = "Foto",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(16.dp),
                tint = navBarChocolate
            )

            Spacer(Modifier.height(24.dp))

            // Card con la información del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Nombre", fontSize = 14.sp, color = Color.Gray)
                    Text(usuario.nombre, fontSize = 16.sp, color = Color.Black)
                    Spacer(Modifier.height(12.dp))

                    Text("Teléfono", fontSize = 14.sp, color = Color.Gray)
                    Text(usuario.telefono, fontSize = 16.sp, color = Color.Black)
                    Spacer(Modifier.height(12.dp))

                    Text("Email", fontSize = 14.sp, color = Color.Gray)
                    Text(usuario.email, fontSize = 16.sp, color = Color.Black)
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onEditarPerfil,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA0E7E5)) // verde menta pastel
            ) {
                Text("Editar Perfil", color = Color.Black)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cerrar sesión", color = Color.White)
            }
        }
    }
}
