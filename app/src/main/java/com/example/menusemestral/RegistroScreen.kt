package com.example.menusemestral

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.menusemestral.ui.theme.Typography

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit
) {
    val context = LocalContext.current
    val nombre = remember { mutableStateOf("") }
    val telefono = remember { mutableStateOf("") }
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }

    val pastelTop = Color(0xFFFFD6A5)
    val pastelBottom = Color(0xFFFFF5BA)
    val mintGreen = Color(0xFFA0E7E5)
    val textDark = Color(0xFF333333)
    val warningColor = Color(0xFFF44336)

    val showWarning = remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        colors = listOf(pastelTop, pastelBottom)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registro de Usuario",
                style = Typography.headlineMedium,
                fontSize = 26.sp,
                color = textDark
            )

            val inputColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Black
            )

            OutlinedTextField(
                value = nombre.value,
                onValueChange = { nombre.value = it },
                placeholder = { Text("Nombre", color = Color.Black) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 4.dp),
                colors = inputColors
            )

            OutlinedTextField(
                value = telefono.value,
                onValueChange = { telefono.value = it },
                placeholder = { Text("Teléfono", color = Color.Black) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 4.dp),
                colors = inputColors
            )

            OutlinedTextField(
                value = correo.value,
                onValueChange = {
                    correo.value = it
                    if (showWarning.value && it.contains("@")) {
                        showWarning.value = false
                    }
                },
                placeholder = { Text("Correo electrónico", color = Color.Black) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 4.dp),
                colors = inputColors
            )

            OutlinedTextField(
                value = contrasena.value,
                onValueChange = { contrasena.value = it },
                placeholder = { Text("Contraseña", color = Color.Black) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 4.dp),
                colors = inputColors
            )

            AnimatedVisibility(visible = showWarning.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(warningColor, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        "Ingrese un correo valido",
                        color = textDark,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Button(
                onClick = {
                    if (!correo.value.contains("@")) {
                        showWarning.value = true
                    } else {
                        showWarning.value = false
                        registrarUsuario(
                            context = context,
                            nombre = nombre.value,
                            telefono = telefono.value,
                            correo = correo.value,
                            contrasena = contrasena.value,
                            onSuccess = onRegistroExitoso
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mintGreen,
                    contentColor = textDark
                )
            ) {
                Text("Registrarse", fontSize = 18.sp)
            }
        }
    }
}
