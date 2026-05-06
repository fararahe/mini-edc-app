package id.faradyna.miniedcapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore untuk penyimpanan data di lokal aplikasi.
 *
 * Menyimpan data penting terkait status dan identitas EDC, seperti:
 * - Status aktivasi device
 * - Data merchant & terminal
 * - Token autentikasi
 */
@Singleton
class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val IS_ACTIVATED = booleanPreferencesKey("is_activated")
        val TERMINAL_ID = stringPreferencesKey("terminal_id")
        val MERCHANT_ID = stringPreferencesKey("merchant_id")
        val MERCHANT_NAME = stringPreferencesKey("merchant_name")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    val isActivated: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_ACTIVATED] ?: false
        }

    val terminalId: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[TERMINAL_ID]
        }

    val merchantId: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[MERCHANT_ID]
        }

    val merchantName: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[MERCHANT_NAME]
        }

    val authToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[AUTH_TOKEN]
        }

    suspend fun saveActivationData(
        terminalId: String,
        merchantId: String,
        merchantName: String,
        token: String
    ) {
        dataStore.edit { preferences ->
            preferences[IS_ACTIVATED] = true
            preferences[TERMINAL_ID] = terminalId
            preferences[MERCHANT_ID] = merchantId
            preferences[MERCHANT_NAME] = merchantName
            preferences[AUTH_TOKEN] = token
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}