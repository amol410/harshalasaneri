package com.healthapp.model

data class HealthRecord(
    val id: String,
    val title: String,
    val date: String,
    val category: String,
    val filePath: String
)