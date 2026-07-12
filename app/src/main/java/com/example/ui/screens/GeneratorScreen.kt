package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BlueBackground
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorScreen(
    type: String,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToResult: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    var projectName by remember { mutableStateOf("") }
    var idea by remember { mutableStateOf("") }
    var advanced by remember { mutableStateOf("") }
    
    Scaffold(
        containerColor = BlueBackground,
        topBar = {
            TopAppBar(
                title = { Text("Generate $type Prompt", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BlueBackground),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = projectName,
                onValueChange = { projectName = it },
                label = { Text("Project Name (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = idea,
                onValueChange = { idea = it },
                label = { Text("Idea Description (Required)") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = advanced,
                onValueChange = { advanced = it },
                label = { Text("Advanced Fields (Tech stack, features)") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    viewModel.generatePrompt(type, projectName, idea, advanced) {
                        onNavigateToResult()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = idea.isNotBlank() && !state.isGenerating,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (state.isGenerating) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Generate (5 Credits)")
                }
            }
            
            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(state.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
