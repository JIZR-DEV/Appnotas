package com.unison.appproductos.ui.theme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.sp

import androidx.compose.material3.Typography

import androidx.compose.ui.text.googlefonts.Font

import androidx.compose.ui.unit.sp
import com.unison.appproductos.R

// Configuración de Google Fonts
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val AppFontFamily = FontFamily(
    Font(googleFont = GoogleFont("ABeeZee"), fontProvider = provider)
)

// Tipografía personalizada
val AppTypography = Typography(
    displayLarge = Typography().displayLarge.copy(
        fontFamily = AppFontFamily,
        fontSize = 36.sp
    ),
    bodyLarge = Typography().bodyLarge.copy(
        fontFamily = AppFontFamily,
        fontSize = 16.sp
    )
)
