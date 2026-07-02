package com.example.frontend.data.model

data class Grade(
    val id: Long? = null,
    val studentId: Long? = null,
    val subject: String,
    val score: Double,
    val assessmentDate: String,
    val remarks: String? = null
)
