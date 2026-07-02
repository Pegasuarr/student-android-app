package com.example.frontend.ui.screen.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.frontend.data.model.Student
import com.example.frontend.ui.components.LoadingView
import com.example.frontend.ui.components.SearchBar
import com.example.frontend.ui.components.StudentCard
import com.example.frontend.ui.viewmodel.AuthViewModel
import com.example.frontend.ui.viewmodel.StudentViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    studentViewModel: StudentViewModel,
    authViewModel: AuthViewModel,
    onStudentClick: (Long) -> Unit,
    onLogoutClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val isAdmin = authViewModel.userRole == "ROLE_ADMIN"

    LaunchedEffect(searchQuery) {
        studentViewModel.loadStudents(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Student Management",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                        authViewModel.username?.let {
                            Text(
                                text = "Welcome, $it (${authViewModel.userRole?.replace("ROLE_", "")})",
                                fontSize = 12.sp,
                                color = Color(0xFF70757A)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout {
                            onLogoutClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Log Out",
                            tint = Color(0xFFE53935)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF0F4FF)
                )
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Color(0xFF6C63FF),
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Student")
                }
            }
        },
        containerColor = Color(0xFFF0F4FF)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = { searchQuery = it },
                    placeholder = "Search by name or email..."
                )

                Spacer(modifier = Modifier.height(8.dp))

                studentViewModel.errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (studentViewModel.students.isEmpty() && !studentViewModel.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No students found.",
                            color = Color(0xFF70757A),
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(studentViewModel.students) { student ->
                            StudentCard(
                                student = student,
                                onClick = {
                                    student.id?.let {
                                        studentViewModel.selectStudent(student)
                                        onStudentClick(it)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            LoadingView(isLoading = studentViewModel.isLoading)
        }
    }

    if (showAddDialog) {
        AddStudentDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { first, last, email, dob ->
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                studentViewModel.createStudent(
                    firstName = first,
                    lastName = last,
                    email = email,
                    dateOfBirth = if (dob.isEmpty()) null else dob,
                    enrollmentDate = today
                ) {
                    showAddDialog = false
                }
            }
        )
    }
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }

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
                    text = "Add New Student",
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
                    placeholder = { Text("e.g. 2005-08-15") },
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
                        Text("Add")
                    }
                }
            }
        }
    }
}
