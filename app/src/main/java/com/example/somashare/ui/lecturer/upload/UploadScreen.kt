// File: ui/lecturer/upload/UploadScreen.kt
package com.example.somashare.ui.lecturer.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    navController: NavController,
    viewModel: UploadViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onFileSelected(it) }
    }

    LaunchedEffect(uiState.uploadSuccess) {
        if (uiState.uploadSuccess) {
            navController.navigate("lecturer_manage") {
                popUpTo("lecturer_upload") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Past Paper") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4F46E5),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { LecturerBottomNavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // File Selection
            Button(
                onClick = { filePickerLauncher.launch("application/pdf") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isUploading
            ) {
                Icon(Icons.Default.AttachFile, null)
                Spacer(Modifier.width(8.dp))
                Text("Select PDF File")
            }

            if (uiState.selectedFileName.isNotEmpty()) {
                Card(colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF3F4F6)
                )) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.PictureAsPdf, null)
                            Text(uiState.selectedFileName)
                        }
                        if (!uiState.isUploading) {
                            IconButton(onClick = { viewModel.clearFile() }) {
                                Icon(Icons.Default.Close, null)
                            }
                        }
                    }
                }

                // Form fields go here...
                // (Unit selection, paper type, year, etc.)

                // Upload Button
                Button(
                    onClick = { viewModel.uploadPaper() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isUploading && viewModel.canUpload()
                ) {
                    if (uiState.isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("${uiState.uploadProgress}%")
                    } else {
                        Icon(Icons.Default.CloudUpload, null)
                        Spacer(Modifier.width(8.dp))
                        Text("UPLOAD")
                    }
                }
            }
        }
    }
}