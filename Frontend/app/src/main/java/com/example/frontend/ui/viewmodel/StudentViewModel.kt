package com.example.frontend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.Student
import com.example.frontend.data.repository.StudentRepository
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {
    private val repository = StudentRepository()

    var students by mutableStateOf<List<Student>>(emptyList())
        private set

    var selectedStudent by mutableStateOf<Student?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadStudents(query: String? = null) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.getAllStudents(query)
            result.fold(
                onSuccess = { list ->
                    students = list
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to load students"
                    isLoading = false
                }
            )
        }
    }

    fun loadStudentById(id: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.getStudentById(id)
            result.fold(
                onSuccess = { student ->
                    selectedStudent = student
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to load student details"
                    isLoading = false
                }
            )
        }
    }

    fun createStudent(
        firstName: String,
        lastName: String,
        email: String,
        dateOfBirth: String?,
        enrollmentDate: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val student = Student(
                firstName = firstName,
                lastName = lastName,
                email = email,
                dateOfBirth = dateOfBirth,
                enrollmentDate = enrollmentDate
            )
            val result = repository.createStudent(student)
            result.fold(
                onSuccess = {
                    isLoading = false
                    loadStudents()
                    onSuccess()
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to create student"
                    isLoading = false
                }
            )
        }
    }

    fun updateStudent(
        id: Long,
        firstName: String,
        lastName: String,
        email: String,
        dateOfBirth: String?,
        enrollmentDate: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val student = Student(
                id = id,
                firstName = firstName,
                lastName = lastName,
                email = email,
                dateOfBirth = dateOfBirth,
                enrollmentDate = enrollmentDate
            )
            val result = repository.updateStudent(id, student)
            result.fold(
                onSuccess = { updated ->
                    selectedStudent = updated
                    isLoading = false
                    loadStudents()
                    onSuccess()
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to update student"
                    isLoading = false
                }
            )
        }
    }

    fun deleteStudent(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.deleteStudent(id)
            result.fold(
                onSuccess = {
                    isLoading = false
                    loadStudents()
                    onSuccess()
                },
                onFailure = { error ->
                    errorMessage = error.localizedMessage ?: "Failed to delete student"
                    isLoading = false
                }
            )
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun selectStudent(student: Student?) {
        selectedStudent = student
    }
}
