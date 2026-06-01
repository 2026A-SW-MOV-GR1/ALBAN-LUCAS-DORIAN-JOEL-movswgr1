package com.example.proyectomoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.proyectomoviles.ui.theme.ProyectoMovilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storageManager = StorageManager(applicationContext)
        
        enableEdgeToEdge()
        setContent {
            ProyectoMovilesTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MainViewModel(storageManager) as T
                        }
                    }
                )
                
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Red (REST)", "Seguridad (Secretos)")

    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.statusBarsPadding())
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedTab) {
                0 -> NetworkModule(viewModel)
                1 -> SecurityModule(viewModel)
            }
        }
    }
}

@Composable
fun NetworkModule(vm: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Módulo de Conectividad REST", style = MaterialTheme.typography.headlineSmall)
        
        OutlinedTextField(
            value = vm.postIdInput,
            onValueChange = { vm.postIdInput = it },
            label = { Text("ID del Post (1-100)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !vm.isLoadingNetwork
        )

        Button(
            onClick = { vm.fetchPost() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !vm.isLoadingNetwork && vm.postIdInput.isNotBlank()
        ) {
            Text(if (vm.isLoadingNetwork) "Cargando..." else "Consultar (GET)")
        }

        Divider()

        OutlinedTextField(
            value = vm.postTitle,
            onValueChange = { vm.postTitle = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !vm.isLoadingNetwork
        )

        OutlinedTextField(
            value = vm.postBody,
            onValueChange = { vm.postBody = it },
            label = { Text("Contenido (Body)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            enabled = !vm.isLoadingNetwork
        )

        Button(
            onClick = { vm.updatePost() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !vm.isLoadingNetwork && vm.postTitle.isNotBlank()
        ) {
            Text("Actualizar (PUT)")
        }

        if (vm.networkMessage.isNotBlank()) {
            Text(
                text = vm.networkMessage,
                color = if (vm.networkMessage.contains("Error")) Color.Red else Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SecurityModule(vm: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Gestión de Secretos", style = MaterialTheme.typography.headlineSmall)

        Text("Seleccionar Mecanismo:", modifier = Modifier.align(Alignment.Start))
        
        StorageType.values().forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = vm.selectedStorage == type,
                    onClick = { vm.selectedStorage = type },
                    enabled = !vm.isLoadingStorage
                )
                Text(text = type.name.replace("_", " "))
            }
        }

        OutlinedTextField(
            value = vm.secretKey,
            onValueChange = { vm.secretKey = it },
            label = { Text("Llave (Key)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !vm.isLoadingStorage
        )

        OutlinedTextField(
            value = vm.secretValue,
            onValueChange = { vm.secretValue = it },
            label = { Text("Valor (Secret)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !vm.isLoadingStorage,
            visualTransformation = if (vm.isSecretVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (vm.isSecretVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (vm.isSecretVisible) "Ocultar secreto" else "Mostrar secreto"

                IconButton(onClick = { vm.isSecretVisible = !vm.isSecretVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { vm.saveSecret() },
                modifier = Modifier.weight(1f),
                enabled = !vm.isLoadingStorage && vm.secretKey.isNotBlank() && vm.secretValue.isNotBlank()
            ) {
                Text("Guardar")
            }
            
            Button(
                onClick = { vm.retrieveSecret() },
                modifier = Modifier.weight(1f),
                enabled = !vm.isLoadingStorage && vm.secretKey.isNotBlank()
            ) {
                Text("Recuperar")
            }
        }

        if (vm.retrievedSecret.isNotBlank()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Referencia al Secreto:", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (vm.isSecretVisible) vm.retrievedSecret else "••••••••",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { vm.isSecretVisible = !vm.isSecretVisible }) {
                            Icon(
                                imageVector = if (vm.isSecretVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "Toggle"
                            )
                        }
                    }
                    
                    Text(
                        text = "(Almacenado vía: ${vm.selectedStorage.name})",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        if (vm.storageMessage.isNotBlank()) {
            Text(
                text = vm.storageMessage,
                color = if (vm.storageMessage.contains("Error")) Color.Red else Color.Blue,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
