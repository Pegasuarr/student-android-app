package com.example.frontend.data.repository

import com.example.frontend.data.model.Grade
import com.example.frontend.data.network.RetrofitClient

class GradeRepository {

    suspend fun getGradesByStudentId(studentId: Long): Result<List<Grade>> {
        return try {
            val response = RetrofitClient.apiService.getGradesByStudentId(studentId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to fetch grades"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addGrade(studentId: Long, grade: Grade): Result<Grade> {
        return try {
            val response = RetrofitClient.apiService.addGradeToStudent(studentId, grade)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to add grade"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateGrade(gradeId: Long, grade: Grade): Result<Grade> {
        return try {
            val response = RetrofitClient.apiService.updateGrade(gradeId, grade)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to update grade"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGrade(gradeId: Long): Result<Unit> {
        return try {
            val response = RetrofitClient.apiService.deleteGrade(gradeId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to delete grade"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
