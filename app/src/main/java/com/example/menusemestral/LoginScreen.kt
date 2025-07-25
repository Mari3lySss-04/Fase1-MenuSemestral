package com.example.menusemestral

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration




// Esta pantalla de Login recibe correo y contraseña del usuario.
// Al iniciar sesión exitosamente, se guarda el token y se llama a onLoginSuccess.


@Composable
fun LoginScreen(onLoginSuccess: (String, String) -> Unit,
                onNavigateToRegistro: () -> Unit) {
    val context = LocalContext.current
    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = correo.value,
            onValueChange = { correo.value = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena.value,
            onValueChange = { contrasena.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                loginCliente(
                    context = context,
                    correo = correo.value,
                    contrasena = contrasena.value,
                    onSuccess = { nombre, token ->
                        Log.d("TOKEN_ENVIADO desde pantalla de login", token) // ← Aquí debes tener esto
                        SessionManager.saveToken(context, token, nombre)
                        onLoginSuccess(nombre, token)
                    }
                )
            }
            ,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(12.dp))
        //boton para pasar a la ´pantalla de registro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            TextButton(onClick = onNavigateToRegistro) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline)
                )
            }
        }



    }
}

// ---
//
// Pantalla de inicio de sesión. Llama a loginCliente(), guarda el token en SessionManager
// y redirige al usuario si las credenciales son correctas.
// ---

