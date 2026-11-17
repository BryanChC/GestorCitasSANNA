package com.charahua.gestocitassanna.presentation.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.charahua.gestocitassanna.presentation.screens.admin.*
import com.charahua.gestocitassanna.presentation.screens.auth.LoginScreen
import com.charahua.gestocitassanna.presentation.screens.auth.RegisterScreen
import com.charahua.gestocitassanna.presentation.screens.bienvenida.WelcomeScreen
import com.charahua.gestocitassanna.presentation.screens.citas.DetalleCitaScreen
import com.charahua.gestocitassanna.presentation.screens.citas.ReservarCitaScreen
import com.charahua.gestocitassanna.presentation.screens.home.HomePacienteScreen
import com.charahua.gestocitassanna.presentation.viewmodel.*

@Composable
fun SannaNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel = remember { AuthViewModel(context.applicationContext as android.app.Application) }
    val citasViewModel = remember { CitasViewModel(context.applicationContext as android.app.Application) }
    val adminViewModel = remember { AdminViewModel(context.applicationContext as android.app.Application) }
    val costosViewModel = remember { CostosViewModel(context.applicationContext as android.app.Application) }

    val isFirstLaunch by authViewModel.isFirstLaunchFlow.collectAsState(initial = true)
    val userId by authViewModel.userIdFlow.collectAsState(initial = -1L)
    val pacienteId by authViewModel.pacienteIdFlow.collectAsState(initial = 1L)
    val userRol by authViewModel.userRolFlow.collectAsState(initial = "")

    val startDestination = when {
        isFirstLaunch -> "welcome"
        userId != -1L && userRol == "ADMIN" -> "home_admin"
        userId != -1L && userRol == "PACIENTE" -> "home_paciente/$userId/$pacienteId"
        else -> "login"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("welcome") {
            WelcomeScreen(
                onGetStarted = {
                    authViewModel.completeFirstLaunch()
                    navController.navigate("login") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { rol, uId, pId ->
                    if (rol == "ADMIN") {
                        navController.navigate("home_admin") { popUpTo("login") { inclusive = true } }
                    } else {
                        navController.navigate("home_paciente/$uId/$pId") { popUpTo("login") { inclusive = true } }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { rol, uId, pId ->
                    navController.navigate("home_paciente/$uId/$pId") { popUpTo("register") { inclusive = true } }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }
        composable(
            route = "home_paciente/{userId}/{pacienteId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.LongType },
                navArgument("pacienteId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val uId = backStackEntry.arguments?.getLong("userId") ?: 1L
            val pId = backStackEntry.arguments?.getLong("pacienteId") ?: 1L
            HomePacienteScreen(
                citasViewModel = citasViewModel,
                pacienteId = pId,
                userId = uId,
                onNavigateToReservar = { navController.navigate("reservar_cita/$pId") },
                onCitaClick = { citaId -> navController.navigate("detalle_cita/$citaId") },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }
        composable(
            route = "reservar_cita/{pacienteId}",
            arguments = listOf(navArgument("pacienteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pId = backStackEntry.arguments?.getLong("pacienteId") ?: 1L
            ReservarCitaScreen(
                viewModel = citasViewModel,
                pacienteId = pId,
                onBookingSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "detalle_cita/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val cId = backStackEntry.arguments?.getLong("citaId") ?: 1L
            DetalleCitaScreen(
                viewModel = citasViewModel,
                citaId = cId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("home_admin") {
            HomeAdminScreen(
                viewModel = adminViewModel,
                onNavigateToSedes = { navController.navigate("gestion_sedes") },
                onNavigateToEspecialidades = { navController.navigate("gestion_especialidades") },
                onNavigateToMedicos = { navController.navigate("gestion_medicos") },
                onNavigateToHorarios = { navController.navigate("gestion_horarios") },
                onNavigateToCitas = { navController.navigate("gestion_citas") },
                onNavigateToSeguros = { navController.navigate("gestion_seguros") },
                onNavigateToCostos = { navController.navigate("gestion_costos") },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }
        composable("gestion_sedes") { GestionSedesScreen(viewModel = adminViewModel, onBack = { navController.popBackStack() }) }
        composable("gestion_especialidades") { GestionEspecialidadesScreen(viewModel = adminViewModel, onBack = { navController.popBackStack() }) }
        composable("gestion_medicos") { GestionMedicosScreen(viewModel = adminViewModel, onBack = { navController.popBackStack() }) }
        composable("gestion_horarios") { GestionHorariosScreen(viewModel = adminViewModel, onBack = { navController.popBackStack() }) }
        composable("gestion_citas") { GestionCitasScreen(viewModel = adminViewModel, onBack = { navController.popBackStack() }) }
        composable("gestion_seguros") { GestionSegurosScreen(viewModel = adminViewModel, onBack = { navController.popBackStack() }) }
        composable("gestion_costos") { GestionCostosScreen(costosViewModel = costosViewModel, onBack = { navController.popBackStack() }) }
    }
}
