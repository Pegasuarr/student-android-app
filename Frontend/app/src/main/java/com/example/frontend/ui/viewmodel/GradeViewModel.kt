package com.example.frontend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Grade
import com.example.frontend.data.repository.GradeRepository
import kotlinx.coroutines.launch

class GradeViewModel : ViewModel() {
    private val repository = GradeRepository()

    var grades by mutableStateOf<List<Grade>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadGrades(studentId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.getGradesByStudentId(studentId)
            result.fold(
                onSuccess = { list ->
                    grades = list
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to load grades"
                    isLoading = false
                }
            )
        }
    }

    fun addGrade(
        studentId: Long,
        subject: String,
        score: Double,
        assessmentDate: String,
        remarks: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val grade = Grade(
                studentId = studentId,
                subject = subject,
                score = score,
                assessmentDate = assessmentDate,
                remarks = remarks
            )
            val result = repository.addGrade(studentId, grade)
            result.fold(
                onSuccess = {
                    isLoading = false
                    loadGrades(studentId)
                    onSuccess()
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to add grade"
                    isLoading = false
                }
            )
        }
    }

    fun updateGrade(
        gradeId: Long,
        studentId: Long,
        subject: String,
        score: Double,
        assessmentDate: String,
        remarks: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val grade = Grade(
                id = gradeId,
                studentId = studentId,
                subject = subject,
                score = score,
                assessmentDate = assessmentDate,
                remarks = remarks
            )
            val result = repository.updateGrade(gradeId, grade)
            result.fold(
                onSuccess = {
                    isLoading = false
                    loadGrades(studentId)
                    onSuccess()
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to update grade"
                    isLoading = false
                }
            )
        }
    }

    fun deleteGrade(gradeId: Long, studentId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.deleteGrade(gradeId)
            result.fold(
                onSuccess = {
                    isLoading = false
                    loadGrades(studentId)
                    onSuccess()
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to delete grade"
                    isLoading = false
                }
            )
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
