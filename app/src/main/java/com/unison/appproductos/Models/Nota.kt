package com.unison.appproductos.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var titulo: String,
    var contenido: String,
    var colorFondo: String, // Almacena el color en formato hexadecimal
    var uriImagen: String? = null // URI de la imagen de fondo si existe
)

