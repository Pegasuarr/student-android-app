package com.example.frontend.ui.screen.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.frontend.data.model.Grade
import com.example.frontend.data.model.Student
import com.example.frontend.ui.components.LoadingView
import com.example.frontend.ui.viewmodel.AuthViewModel
import com.example.frontend.ui.viewmodel.GradeViewModel
import com.example.frontend.ui.viewmodel.StudentViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(
    studentId: Long,
    studentViewModel: StudentViewModel,
    gradeViewModel: GradeViewModel,
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onStudentDeleted: () -> Unit
) {
    val student = studentViewModel.selectedStudent
    val isAdmin = authViewModel.userRole == "ROLE_ADMIN"

    var showEditStudentDialog by remember { mutableStateOf(false) }
    var showAddGradeDialog by remember { mutableStateOf(false) }
    var editingGrade by remember { mutableStateOf<Grade?>(null) }

    LaunchedEffect(studentId) {
        studentViewModel.loadStudentById(studentId)
        gradeViewModel.loadGrades(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Student Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (isAdmin && student != null) {
                        IconButton(onClick = {
                            studentViewModel.deleteStudent(studentId) {
                                onStudentDeleted()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Student",
                                tint = Color(0xFFE53935)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF0F4FF)
                )
            )
        },
        containerColor = Color(0xFFF0F4FF)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (student == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!studentViewModel.isLoading) {
                        Text(text = "Student not found", color = Color.Red)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Student Info Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = student.fullName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1A2E)
                                )
                                if (isAdmin) {
                                    IconButton(onClick = { showEditStudentDialog = true }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Student",
                                            tint = Color(0xFF6C63FF)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Email: ${student.email}", fontSize = 14.sp, color = Color(0xFF70757A))
                            student.dateOfBirth?.let {
                                Text(text = "DOB: $it", fontSize = 14.sp, color = Color(0xFF70757A))
                            }
                            Text(text = "Enrollment Date: ${student.enrollmentDate}", fontSize = 14.sp, color = Color(0xFF70757A))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Grades Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Grades & Assessments",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                        if (isAdmin) {
                            Button(
                                onClick = { showAddGradeDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Grade",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Grade", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (gradeViewModel.grades.isEmpty() && !gradeViewModel.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No grades recorded for this student.", color = Color(0xFF70757A))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(gradeViewModel.grades) { grade ->
                                GradeItemCard(
                                    grade = grade,
                                    isAdmin = isAdmin,
                                    onEditClick = { editingGrade = grade },
                                    onDeleteClick = {
                                        grade.id?.let { gid ->
                                            gradeViewModel.deleteGrade(gid, studentId) {}
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            LoadingView(isLoading = studentViewModel.isLoading || gradeViewModel.isLoading)
        }
    }

    if (showEditStudentDialog && student != null) {
        EditStudentDialog(
            student = student,
            onDismiss = { showEditStudentDialog = false },
            onConfirm = { first, last, email, dob ->
                studentViewModel.updateStudent(
                    id = studentId,
                    firstName = first,
                    lastName = last,
                    email = email,
                    dateOfBirth = if (dob.isEmpty()) null else dob,
                    enrollmentDate = student.enrollmentDate
                ) {
                    showEditStudentDialog = false
                }
            }
        )
    }

    if (showAddGradeDialog) {
        AddEditGradeDialog(
            grade = null,
            onDismiss = { showAddGradeDialog = false },
            onConfirm = { subject, score, remarks ->
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                gradeViewModel.addGrade(
                    studentId = studentId,
                    subject = subject,
                    score = score,
                    assessmentDate = today,
                    remarks = if (remarks.isEmpty()) null else remarks
                ) {
                    showAddGradeDialog = false
                }
            }
        )
    }

    if (editingGrade != null) {
        AddEditGradeDialog(
            grade = editingGrade,
            onDismiss = { editingGrade = null },
            onConfirm = { subject, score, remarks ->
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                gradeViewModel.updateGrade(
                    gradeId = editingGrade!!.id!!,
                    studentId = studentId,
                    subject = subject,
                    score = score,
                    assessmentDate = today,
                    remarks = if (remarks.isEmpty()) null else remarks
                ) {
                    editingGrade = null
                }
            }
        )
    }
}

@Composable
fun GradeItemCard(
    grade: Grade,
    isAdmin: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = grade.subject,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1C1B1F)
                )
                Text(
                    text = "Score: ${grade.score}/100",
                    fontSize = 14.sp,
                    color = if (grade.score >= 50) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.SemiBold
                )
                grade.remarks?.let {
                    Text(
                        text = "Remarks: $it",
                        fontSize = 12.sp,
                        color = Color(0xFF70757A)
                    )
                }
                Text(
                    text = "Date: ${grade.assessmentDate}",
                    fontSize = 10.sp,
                    color = Color(0xFFB0B0B0)
                )
            }

            if (isAdmin) {
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Grade", tint = Color(0xFF6C63FF))
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Grade", tint = Color(0xFFE53935))
                    }
                }
            }
        }
    }
}

@Composable
fun EditStudentDialog(
    student: Student,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var firstName by remember { mutableStateOf(student.firstName) }
    var lastName by remember { mutableStateOf(student.lastName) }
    var email by remember { mutableStateOf(student.email) }
    var dob by remember { mutableStateOf(student.dateOfBirth ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit Student Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dob,
                    onValueChange = { dob = it },
                    label = { Text("Date of Birth (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF70757A))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()) {
                                onConfirm(firstName, lastName, email, dob)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditGradeDialog(
    grade: Grade?,
    onDismiss: () -> Unit,
    onConfirm: (String, Double, String) -> Unit
) {
    var subject by remember { mutableStateOf(grade?.subject ?: "") }
    var scoreStr by remember { mutableStateOf(grade?.score?.toString() ?: "") }
    var remarks by remember { mutableStateOf(grade?.remarks ?: "") }

    val isEdit = grade != null

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isEdit) "Edit Grade" else "Add Student Grade",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = scoreStr,
                    onValueChange = { scoreStr = it },
                    label = { Text("Score (0 - 100)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Remarks") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF70757A))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val score = scoreStr.toDoubleOrNull()
                            if (subject.isNotEmpty() && score != null && score in 0.0..100.0) {
                                onConfirm(subject, score, remarks)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                    ) {
                        Text(if (isEdit) "Save" else "Add")
                    }
                }
            }
        }
    }
}
