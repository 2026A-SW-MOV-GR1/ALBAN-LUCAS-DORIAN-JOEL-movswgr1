package com.example.proyectomoviles

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val storageManager: StorageManager) : ViewModel() {

    // --- State for Network Module ---
    var postIdInput by mutableStateOf("")
    var postTitle by mutableStateOf("")
    var postBody by mutableStateOf("")
    var isLoadingNetwork by mutableStateOf(false)
    var networkMessage by mutableStateOf("")
    
    // Mapa local para simular persistencia de la API falsa
    private val localPostsCache = mutableMapOf<Int, Post>()

    fun fetchPost() {
        val id = postIdInput.toIntOrNull() ?: return
        viewModelScope.launch {
            isLoadingNetwork = true
            networkMessage = ""
            try {
                // Si existe en cache local (actualizado), lo usamos. Si no, consultamos API.
                val post = localPostsCache[id] ?: RetrofitClient.instance.getPost(id)
                postTitle = post.title
                postBody = post.body
                networkMessage = "Post cargado con éxito"
            } catch (e: Exception) {
                networkMessage = "Error: ${e.message}"
            } finally {
                isLoadingNetwork = false
            }
        }
    }

    fun updatePost() {
        val id = postIdInput.toIntOrNull() ?: return
        viewModelScope.launch {
            isLoadingNetwork = true
            networkMessage = ""
            try {
                val updatedPost = Post(id = id, title = postTitle, body = postBody, userId = 1)
                RetrofitClient.instance.updatePost(id, updatedPost)
                
                // Guardamos localmente para que el GET posterior lo "recuerde"
                localPostsCache[id] = updatedPost
                
                networkMessage = "Actualización exitosa (200 OK) y guardada localmente"
            } catch (e: Exception) {
                networkMessage = "Error al actualizar: ${e.message}"
            } finally {
                isLoadingNetwork = false
            }
        }
    }

    // --- State for Security Module ---
    var secretKey by mutableStateOf("")
    var secretValue by mutableStateOf("")
    var selectedStorage by mutableStateOf(StorageType.SHARED_PREFERENCES)
    var retrievedSecret by mutableStateOf("")
    var isSecretVisible by mutableStateOf(false) // Nuevo: Control de visibilidad
    var storageMessage by mutableStateOf("")
    var isLoadingStorage by mutableStateOf(false)

    fun saveSecret() {
        if (secretKey.isBlank() || secretValue.isBlank()) return
        viewModelScope.launch {
            isLoadingStorage = true
            try {
                storageManager.saveSecret(selectedStorage, secretKey, secretValue)
                storageMessage = "Secreto guardado en ${selectedStorage.name}"
                secretValue = "" // Limpiar campo después de guardar
            } catch (e: Exception) {
                storageMessage = "Error al guardar: ${e.message}"
            } finally {
                isLoadingStorage = false
            }
        }
    }

    fun retrieveSecret() {
        if (secretKey.isBlank()) return
        viewModelScope.launch {
            isLoadingStorage = true
            try {
                val value = storageManager.getSecret(selectedStorage, secretKey)
                if (value != null) {
                    retrievedSecret = value
                    storageMessage = "Secreto recuperado"
                } else {
                    retrievedSecret = ""
                    storageMessage = "El secreto no existe en este compartimento"
                }
            } catch (e: Exception) {
                storageMessage = "Error al recuperar: ${e.message}"
            } finally {
                isLoadingStorage = false
            }
        }
    }
}
