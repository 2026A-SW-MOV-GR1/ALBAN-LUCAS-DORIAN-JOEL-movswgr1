package com.example.proyectomoviles

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// --- NETWORK LAYER ---
data class Post(val id: Int? = null, val title: String, val body: String, val userId: Int)

interface ApiService {
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Post

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): Post
}

object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// --- STORAGE LAYER ---
enum class StorageType {
    SHARED_PREFERENCES,
    DATA_STORE,
    ENCRYPTED_PREFS
}

private val Context.dataStore by preferencesDataStore(name = "secrets_store")

class StorageManager(private val context: Context) {

    // 1. SharedPreferences (Texto plano)
    private val sharedPrefs = context.getSharedPreferences("normal_prefs", Context.MODE_PRIVATE)

    // 2. EncryptedSharedPreferences (AES-256)
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val encryptedPrefs = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun saveSecret(type: StorageType, key: String, value: String) {
        when (type) {
            StorageType.SHARED_PREFERENCES -> sharedPrefs.edit().putString(key, value).apply()
            StorageType.ENCRYPTED_PREFS -> encryptedPrefs.edit().putString(key, value).apply()
            StorageType.DATA_STORE -> {
                val dataStoreKey = stringPreferencesKey(key)
                context.dataStore.edit { it[dataStoreKey] = value }
            }
        }
    }

    suspend fun getSecret(type: StorageType, key: String): String? {
        return when (type) {
            StorageType.SHARED_PREFERENCES -> sharedPrefs.getString(key, null)
            StorageType.ENCRYPTED_PREFS -> encryptedPrefs.getString(key, null)
            StorageType.DATA_STORE -> {
                val dataStoreKey = stringPreferencesKey(key)
                context.dataStore.data.map { it[dataStoreKey] }.first()
            }
        }
    }
}
