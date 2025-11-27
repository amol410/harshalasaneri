package com.healthapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthapp.screens.UploadedFile
import com.healthapp.screens.Vaccination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HealthAppViewModel : ViewModel() {
    private val _uploadedFiles = MutableStateFlow<List<UploadedFile>>(emptyList())
    val uploadedFiles: StateFlow<List<UploadedFile>> = _uploadedFiles
    
    private val _vaccinations = MutableStateFlow<List<Vaccination>>(emptyList())
    val vaccinations: StateFlow<List<Vaccination>> = _vaccinations

    fun addFile(file: UploadedFile) {
        viewModelScope.launch {
            _uploadedFiles.value = _uploadedFiles.value + file
        }
    }

    fun deleteFile(id: String) {
        viewModelScope.launch {
            _uploadedFiles.value = _uploadedFiles.value.filter { it.id != id }
        }
    }

    fun addVaccination(vaccination: Vaccination) {
        viewModelScope.launch {
            _vaccinations.value = _vaccinations.value + vaccination
        }
    }

    fun deleteVaccination(id: String) {
        viewModelScope.launch {
            _vaccinations.value = _vaccinations.value.filter { it.id != id }
        }
    }
}