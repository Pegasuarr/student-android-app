package com.example.frontend.data.network

import com.example.frontend.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("students")
    suspend fun getAllStudents(@Query("query") query: String? = null): Response<List<Student>>

    @GET("students/{id}")
    suspend fun getStudentById(@Path("id") id: Long): Response<Student>

    @POST("students")
    suspend fun createStudent(@Body student: Student): Response<Student>

    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") id: Long, @Body student: Student): Response<Student>

    @DELETE("students/{id}")
    suspend fun deleteStudent(@Path("id") id: Long): Response<Void>

    @GET("students/{studentId}/grades")
    suspend fun getGradesByStudentId(@Path("studentId") studentId: Long): Response<List<Grade>>

    @POST("students/{studentId}/grades")
    suspend fun addGradeToStudent(
        @Path("studentId") studentId: Long,
        @Body grade: Grade
    ): Response<Grade>

    @PUT("grades/{gradeId}")
    suspend fun updateGrade(
        @Path("gradeId") gradeId: Long,
        @Body grade: Grade
    ): Response<Grade>

    @DELETE("grades/{gradeId}")
    suspend fun deleteGrade(@Path("gradeId") gradeId: Long): Response<Void>
}
