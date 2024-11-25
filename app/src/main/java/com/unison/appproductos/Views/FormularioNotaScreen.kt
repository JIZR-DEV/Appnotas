// FormularioNotaScreen.kt
package com.unison.appproductos.Views

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.unison.appproductos.Models.Nota
import com.unison.appproductos.ViewModel.NotaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioNotaScreen(navController: NavController, viewModel: NotaViewModel, notaId: Int?) {
    val estadoUi by viewModel.estadoUi.collectAsState()
    val notaExistente = estadoUi.notas.find { it.id == notaId }

    // Usar rememberSaveable para persistir el estado a través de cambios de configuración
    var titulo by rememberSaveable { mutableStateOf(notaExistente?.titulo ?: "") }
    var contenido by rememberSaveable { mutableStateOf(notaExistente?.contenido ?: "") }
    var colorFondo by rememberSaveable { mutableStateOf(notaExistente?.colorFondo ?: "#FFFFFF") }
    var uriImagen by rememberSaveable { mutableStateOf(notaExistente?.uriImagen) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    // Función para ocultar el teclado
    fun hideKeyboard() {
        focusManager.clearFocus()
    }

    // Obtener el SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    // Lanzador para tomar foto con la cámara
    val photoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        coroutineScope.launch {
            bitmap?.let {
                val rutaArchivo = guardarBitmapEnRuta(context, it)
                if (rutaArchivo != null) {
                    uriImagen = rutaArchivo  // Almacena la ruta del archivo
                } else {
                    // Mostrar error si no se pudo guardar la imagen
                    snackbarHostState.showSnackbar("Error al guardar la imagen.")
                }
            }
        }
    }

    // Lanzador para seleccionar imagen de la galería
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        coroutineScope.launch {
            uri?.let {
                val rutaArchivo = copiarUriAArchivo(context, it)
                if (rutaArchivo != null) {
                    uriImagen = rutaArchivo  // Almacena la ruta del archivo copiado
                } else {
                    // Mostrar error si no se pudo copiar la imagen
                    snackbarHostState.showSnackbar("Error al seleccionar la imagen.")
                }
            }
        }
    }

    // Observar mensajes de la ViewModel
    LaunchedEffect(key1 = estadoUi.mensaje) {
        estadoUi.mensaje?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.clearMensaje() // Limpiar el mensaje después de mostrar el Snackbar
            navController.navigate("listaNotas") {
                popUpTo("listaNotas") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (notaExistente != null) "Editar Nota" else "Nueva Nota") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, // Agregar SnackbarHost
        // Se ha eliminado el FloatingActionButton
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .clickable { hideKeyboard() }
            ) {
                // Mostrar imagen de fondo si existe
                if (uriImagen != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = File(uriImagen!!)),
                        contentDescription = "Imagen de fondo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Mostrar color de fondo
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(android.graphics.Color.parseColor(colorFondo)))
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    // Campo de Título
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título", color = obtenerColorContraste(colorFondo)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = obtenerColorContraste(colorFondo),
                            cursorColor = obtenerColorContraste(colorFondo),
                            unfocusedLabelColor = obtenerColorContraste(colorFondo)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    // Campo de Descripción
                    OutlinedTextField(
                        value = contenido,
                        onValueChange = { contenido = it },
                        label = { Text("Descripción", color = obtenerColorContraste(colorFondo)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = obtenerColorContraste(colorFondo),
                            cursorColor = obtenerColorContraste(colorFondo),
                            unfocusedLabelColor = obtenerColorContraste(colorFondo)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(bottom = 16.dp)
                    )

                    // Botón para tomar foto
                    Button(
                        onClick = { photoLauncher.launch() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50), // Verde llamativo
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Camera, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tomar Foto para Fondo", fontWeight = FontWeight.Bold)
                    }

                    // Botón para seleccionar imagen de la galería
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3), // Azul llamativo
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Image, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Seleccionar Imagen de la Galería", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector de color
                    Text(
                        "Seleccionar color:",
                        style = MaterialTheme.typography.bodyMedium.copy(color = obtenerColorContraste(colorFondo)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    SelectorColor(
                        colorSeleccionado = colorFondo,
                        onColorSeleccionado = { color ->
                            colorFondo = color
                            uriImagen = null  // Eliminar imagen al seleccionar un color
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de Crear o Actualizar
                    Button(
                        onClick = {
                            val nuevaNota = Nota(
                                id = notaExistente?.id ?: generarNuevoId(viewModel),
                                titulo = titulo,
                                contenido = contenido,
                                colorFondo = colorFondo,
                                uriImagen = uriImagen
                            )
                            if (notaExistente != null) {
                                viewModel.actualizarNota(nuevaNota)
                            } else {
                                viewModel.agregarNota(nuevaNota)
                            }
                            // La navegación y el Snackbar se manejan en LaunchedEffect
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EA), // Morado oscuro
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (notaExistente != null) "Actualizar Nota" else "Crear Nota",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}

/**
 * Composable para seleccionar un color de una lista predefinida.
 */
@Composable
fun SelectorColor(colorSeleccionado: String, onColorSeleccionado: (String) -> Unit) {
    val colores = listOf("#4A90E2", "#7986CB", "#AED581", "#FFCC80", "#FFFFFF")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        colores.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(android.graphics.Color.parseColor(color)), shape = CircleShape)
                    .border(
                        width = if (color == colorSeleccionado) 2.dp else 1.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
                    .clickable { onColorSeleccionado(color) }
            )
        }
    }
}

/**
 * Función para generar un nuevo ID para una nota.
 * Asegúrate de que esta función esté en tu ViewModel o en un lugar adecuado.
 */
private fun generarNuevoId(viewModel: NotaViewModel): Int {
    // Implementa la lógica para generar un nuevo ID único
    // Aquí hay un ejemplo simple:
    return viewModel.estadoUi.value.notas.maxOfOrNull { it.id }?.plus(1) ?: 1
}

/**
 * Función para guardar el Bitmap como una ruta de archivo y obtener su ruta absoluta.
 * Esta función ahora se ejecuta en un hilo de fondo.
 */
suspend fun guardarBitmapEnRuta(context: android.content.Context, bitmap: Bitmap): String? {
    return withContext(Dispatchers.IO) {
        val carpetaImagenes = File(context.cacheDir, "imagenes")
        try {
            carpetaImagenes.mkdirs()
            val archivo = File(carpetaImagenes, "${System.currentTimeMillis()}.png")
            val stream = FileOutputStream(archivo)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            archivo.absolutePath  // Retorna la ruta absoluta
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

/**
 * Función para copiar el contenido de una URI a un archivo en el directorio de la aplicación.
 * Esta función ahora se ejecuta en un hilo de fondo.
 */
suspend fun copiarUriAArchivo(context: android.content.Context, uri: Uri): String? {
    return withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        try {
            val inputStream = resolver.openInputStream(uri) ?: return@withContext null
            val carpetaImagenes = File(context.cacheDir, "imagenes")
            carpetaImagenes.mkdirs()
            val archivo = File(carpetaImagenes, "${System.currentTimeMillis()}.png")
            val outputStream = FileOutputStream(archivo)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            archivo.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
