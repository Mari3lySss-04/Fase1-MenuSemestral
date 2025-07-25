package com.example.menusemestral

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.menusemestral.ui.theme.Typography

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit,
    onNavigateToRegistro: () -> Unit
) {
    val context = LocalContext.current
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }

    // Colores pastel
    val pastelTop = Color(0xFFFFD6A5)
    val pastelBottom = Color(0xFFFFF5BA)
    val mintGreen = Color(0xFFA0E7E5)
    val textDark = Color(0xFF333333)

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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo Pizzería",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Iniciar sesión",
                color = textDark,
                fontSize = 30.sp,
                style = Typography.headlineMedium
            )

            OutlinedTextField(
                value = correo.value,
                onValueChange = { correo.value = it },
                placeholder = { Text("Correo electrónico", color = Color.Black) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.Black
                )
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )

            Button(
                onClick = {
                    loginCliente(
                        context = context,
                        correo = correo.value,
                        contrasena = contrasena.value,
                        onSuccess = { nombre, token ->
                            Log.d("TOKEN_ENVIADO desde pantalla de login", token)
                            SessionManager.saveToken(context, token, nombre)
                            onLoginSuccess(nombre, token)
                        }
                    )
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
                Text("Aceptar", fontSize = 18.sp)
            }

            TextButton(onClick = onNavigateToRegistro) {
                Text(
                    text = "¿No tienes cuenta? Regístrate aquí",
                    color = textDark,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}
