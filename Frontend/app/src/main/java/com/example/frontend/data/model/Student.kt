package com.example.frontend.data.model

data class Student(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val dateOfBirth: String?,
    val enrollmentDate: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}
