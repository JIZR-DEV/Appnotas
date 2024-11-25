package com.unison.appproductos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.unison.appproductos.Navigation.GrafoNavegacion
import com.unison.appproductos.ui.theme.AppProductosTheme // Cambiar a AppProductosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configurar el contenido usando AppProductosTheme y GrafoNavegacion
        setContent {
            AppProductosTheme { // Cambiar a AppProductosTheme
                GrafoNavegacion()
            }
        }
    }
}
