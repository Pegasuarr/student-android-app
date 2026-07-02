package com.example.frontend.data.repository

import com.example.frontend.data.model.Student
import com.example.frontend.data.network.RetrofitClient

class StudentRepository {

    suspend fun getAllStudents(query: String? = null): Result<List<Student>> {
        return try {
            val response = RetrofitClient.apiService.getAllStudents(query)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to fetch students"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStudentById(id: Long): Result<Student> {
        return try {
            val response = RetrofitClient.apiService.getStudentById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: "Student not found"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createStudent(student: Student): Result<Student> {
        return try {
            val response = RetrofitClient.apiService.createStudent(student)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to create student"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateStudent(id: Long, student: Student): Result<Student> {
        return try {
            val response = RetrofitClient.apiService.updateStudent(id, student)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to update student"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteStudent(id: Long): Result<Unit> {
        return try {
            val response = RetrofitClient.apiService.deleteStudent(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to delete student"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
