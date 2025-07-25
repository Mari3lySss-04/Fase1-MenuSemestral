package com.example.menusemestral


import com.example.menusemestral.models.Producto
import com.example.menusemestral.models.ProductosResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST


import retrofit2.Call
import retrofit2.http.*


//ESTOz SON LOS END POINTS DEL BACKEND
// Interfaz que define los endpoints HTTP disponibles en el backend para login, registro, compras, perfil, etc.


interface ApiService {
    @POST("sessions/login.php")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("sessions/registro.php")
    fun registrar(@Body request: RegistroRequest): Call<RegistroResponse>

    @GET("sessions/productos/lista.php")
    fun getProductos(@Header("Authorization") token: String): Call<ProductosResponse>


    @POST("sessions/compras/comprar.php")
    fun realizarCompra(
        @Header("Authorization") token: String,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Call<Map<String, Any>>

    @GET("sessions/compras/historial.php")
    fun obtenerHistorial(
        @Header("Authorization") token: String
    ): Call<HistorialResponse>

    @GET("sessions/compras/perfil.php")
    fun obtenerPerfil(
        @Header("Authorization") token: String
    ): Call<PerfilResponse>

    @POST("sessions/compras/actualizar_perfil.php")
    fun actualizarPerfil(
        @Header("Authorization") token: String,
        @Body usuario: UsuarioEditado,
        @Query("password") password: String
    ): Call<GenericResponse>

    @POST("sessions/compras/cambiar_password.php")
    fun cambiarPassword(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Call<GenericResponse>





    //  a

    @Multipart
    @POST("ruta_php/subir_imagen.php")
    fun subirImagen(
        @Part imagen: MultipartBody.Part
    ): Call<ResponseBody>

}
/*
*
* Interfaz central de Retrofit donde se declaran los endpoints HTTP disponibles en el servidor PHP.
* Aquí se definen métodos para login, registro, obtener productos, historial, perfil, actualizar perfil,
* cambiar contraseña y subir imágenes.
*
* Se utiliza a través de RetrofitInstance y es invocada desde las pantallas o funciones como loginCliente,
* registrarUsuario, actualizarPerfil, etc.
*/
