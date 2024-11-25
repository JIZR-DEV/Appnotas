package com.unison.appproductos.Navigation

import  com.unison.appproductos.Views.FormularioNotaScreen
import androidx.navigation.navArgument
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.room.Room
import com.unison.appproductos.ViewModel.NotaViewModel
import com.unison.appproductos.ViewModel.NotaViewModelFactory
import com.unison.appproductos.room.AppDatabase
import com.unison.appproductos.room.NotaRepositorio
import com.unison.appproductos.Views.*

import com.unison.appproductos.Views.HomeScreen

@Composable
fun GrafoNavegacion(startDestination: String = "inicio") {
    val navController = rememberNavController()

    // Inicializar ViewModel
    val context = LocalContext.current
    val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "notas-db"
    ).build()
    val repositorio = NotaRepositorio(database.notaDao())
    val viewModelFactory = NotaViewModelFactory(repositorio)
    val viewModel: NotaViewModel = viewModel(factory = viewModelFactory)

    NavHost(navController = navController, startDestination = startDestination) {
        composable("inicio") {
            HomeScreen(navController = navController)
        }
        composable("listaNotas") {
            ListaNotasScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = "formularioNota?notaId={notaId}",
            arguments = listOf(navArgument("notaId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val notaId = backStackEntry.arguments?.getInt("notaId") ?: -1
            FormularioNotaScreen(navController = navController, viewModel = viewModel, notaId = notaId)
        }
        composable(
            route = "detalleNota/{notaId}",
            arguments = listOf(navArgument("notaId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val notaId = backStackEntry.arguments?.getInt("notaId") ?: -1
            DetalleNotaScreen(navController = navController, viewModel = viewModel, notaId = notaId)
        }
    }
}
