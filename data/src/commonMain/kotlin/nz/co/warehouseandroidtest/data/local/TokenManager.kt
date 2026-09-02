package nz.co.warehouseandroidtest.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class TokenManager(
    private val dataStore: DataStore<Preferences>
) {
    val token: Flow<String?> = dataStore.data.map { it[KEY_TOKEN] }

    suspend fun read(): String? = token.first()

    suspend fun save(token: String) {
        dataStore.edit { it[KEY_TOKEN] = token }
    }

    suspend fun clear() {
        dataStore.edit { it.remove(KEY_TOKEN) }
    }

    private companion object {
        val KEY_TOKEN = stringPreferencesKey("twl_token")
    }
}
