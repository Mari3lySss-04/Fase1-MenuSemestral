package com.example.menusemestral

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.example.menusemestral.ui.theme.MenuSemestralTheme
import org.json.JSONObject
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import com.example.menusemestral.models.CarritoItem
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

// MainActivity controla toda la navegación entre pantallas y mantiene estados como el carrito y sesión del usuario.
// Utiliza SessionManager para manejar la persistencia del token, nombre, email y teléfono del usuario.



sealed class Pantalla(val ruta: String) {
    object Login : Pantalla("login")
    object Registro : Pantalla("registro")
    object Home : Pantalla("home")
    object Carrito : Pantalla("carrito")
    object Historial : Pantalla("historial")
    object Perfil : Pantalla("perfil")
    object EditarPerfil : Pantalla("editar_perfil") // estsss

}




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MenuSemestralTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val carritoItems = remember { mutableStateListOf<CarritoItem>() }
                val historial = remember { mutableStateListOf<CompraConDetalles>() }
                val usuario = Usuario(
                    nombre = SessionManager.fetchNombre(context),
                    telefono = SessionManager.fetchTelefono(context),
                    email = SessionManager.fetchEmail(context)
                )


                NavHost(navController = navController, startDestination = Pantalla.Login.ruta) {
                    composable(Pantalla.Login.ruta) {
                        LoginScreen(
                            onLoginSuccess = { nombre, token ->
                                //Toast.makeText(context, "Login exitoso de $nombre", Toast.LENGTH_SHORT).show()

                                navController.navigate(Pantalla.Home.ruta)
                            },
                            onNavigateToRegistro = {
                                navController.navigate(Pantalla.Registro.ruta)
                            }
                        )
                    }
                    composable(Pantalla.Registro.ruta) {
                        RegistroScreen(
                            onRegistroExitoso = {
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                navController.popBackStack() // vuelve al login
                            }
                        )
                    }

                    composable(Pantalla.Home.ruta) {
                        val nombreUsuario = SessionManager.fetchNombre(context)
                        ProductosScreen(

                            nombreUsuario = nombreUsuario,
                            onLogout = {
                                SessionManager.clearToken(context)
                                navController.navigate(Pantalla.Login.ruta) {
                                    popUpTo(Pantalla.Home.ruta) { inclusive = true }
                                }
                            },
                            onGoToCarrito = {
                                navController.navigate(Pantalla.Carrito.ruta)
                            },
                            onNavChange = { ruta ->
                                navController.navigate(ruta)
                            }, carritoItems = carritoItems
                        )
                    }


                    composable(Pantalla.Carrito.ruta) {
                        CarritoScreen(
                            carritoItems = carritoItems,
                            onClearCart = {
                                carritoItems.clear()
                                navController.popBackStack()
                            }, onNavChange = { ruta ->
                                navController.navigate(ruta)
                            }
                        )
                    }


                    composable("historial") {
                        val token = SessionManager.fetchToken(context).let { if (it.isNotEmpty()) "Bearer $it" else "" }

                        LaunchedEffect(Unit) {
                            RetrofitInstance.api.obtenerHistorial(token).enqueue(object :Callback<HistorialResponse> {
                                override fun onResponse(call: Call<HistorialResponse>, response: Response<HistorialResponse>) {
                                    if (response.isSuccessful && response.body()?.success == true) {
                                        historial.clear()
                                        historial.addAll(response.body()!!.historial)
                                    } else {
                                        Toast.makeText(context, "Error al cargar historial", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<HistorialResponse>, t: Throwable) {
                                    Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }

                        HistorialScreen(historial = historial, onNavChange = { navController.navigate(it) })
                    }



                    composable(Pantalla.Perfil.ruta) {
                        val usuarioActual = Usuario(
                            nombre = SessionManager.fetchNombre(context),
                            telefono = SessionManager.fetchTelefono(context),
                            email = SessionManager.fetchEmail(context)
                        )
                        PerfilEstaticaScreen(
                            usuario = usuarioActual,
                            onLogout = {
                                SessionManager.clearToken(context)
                                navController.navigate(Pantalla.Login.ruta) {
                                    popUpTo(Pantalla.Home.ruta) { inclusive = true }
                                }
                            },
                            onNavChange = { ruta -> navController.navigate(ruta) },
                            onEditarPerfil = {
                                navController.navigate(Pantalla.EditarPerfil.ruta)
                            }
                        )
                    }

                    composable(Pantalla.EditarPerfil.ruta) {
                        val usuarioActual = Usuario(
                            nombre = SessionManager.fetchNombre(context),
                            telefono = SessionManager.fetchTelefono(context),
                            email = SessionManager.fetchEmail(context)
                        )

                        PerfilScreen(
                            usuario = usuarioActual,
                            onLogout = {
                                SessionManager.clearToken(context)
                                navController.navigate(Pantalla.Login.ruta) {
                                    popUpTo(Pantalla.Home.ruta) { inclusive = true }
                                }
                            },
                            onNavChange = { ruta -> navController.navigate(ruta) },
                            onActualizarPerfil = { nuevoUsuario, passActual ->
                                actualizarPerfil(context, nuevoUsuario, passActual) { exito ->
                                    if (exito) {
                                        // aca uarda los nuevos datos en SessionManager
                                        SessionManager.saveToken(context, SessionManager.fetchToken(context), nuevoUsuario.nombre)
                                        SessionManager.saveTelefono(context, nuevoUsuario.telefono)
                                        SessionManager.saveEmail(context, nuevoUsuario.email)

                                        navController.navigate(Pantalla.Perfil.ruta) {
                                            popUpTo(Pantalla.Perfil.ruta) { inclusive = true }
                                        }
                                    }

                                }
                            },
                            onCambiarPassword = { passActual, nuevaPass ->
                                cambiarPassword(context, passActual, nuevaPass) { exito ->
                                    if (exito) {
                                        navController.navigate(Pantalla.Perfil.ruta)
                                    }
                                }
                            }
                        )
                    }





                }

                }
            }
        }
    }


/*
*
* Actividad principal de la app. Aquí se configuran todas las rutas de navegación usando Jetpack Navigation Compose.
* Se inicializan estados globales como el carrito de compras, historial, y los datos del usuario.
*
* Conecta con SessionManager para manejar la sesión del usuario.
* Navega entre Login, Registro, Home, Carrito, Historial, Perfil, y Editar Perfil.
* Llama funciones de API como obtener historial, actualizar perfil o cambiar contraseña.
*/
