package com.healthapp.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.healthapp.screens.*
import com.healthapp.viewmodel.HealthAppViewModel

@Composable
fun HealthAppNavigation(viewModel: HealthAppViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            AuthScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("medicine-reminder") {
            MedicineReminderScreen(navController = navController)
        }
        composable("upload-record") {
            UploadRecordScreen(
                navController = navController,
                onFileUpload = { file ->
                    viewModel.addFile(file)
                },
                uploadedFiles = viewModel.uploadedFiles.value,
                onDeleteFile = { id ->
                    viewModel.deleteFile(id)
                }
            )
        }
        composable("profile") {
            ProfileScreen(navController = navController) {
                // Handle logout
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
        composable("history") {
            HistoryScreen(
                navController = navController,
                uploadedFiles = viewModel.uploadedFiles.value,
                onDeleteFile = { id ->
                    viewModel.deleteFile(id)
                }
            )
        }
        composable("doctors-appointment") {
            DoctorsAppointmentScreen(navController = navController)
        }
        composable("vaccination-record") {
            VaccinationRecordScreen(
                navController = navController,
                onVaccinationAdd = { vaccination ->
                    viewModel.addVaccination(vaccination)
                },
                onVaccinationDelete = { id ->
                    viewModel.deleteVaccination(id)
                }
            )
        }
    }
}