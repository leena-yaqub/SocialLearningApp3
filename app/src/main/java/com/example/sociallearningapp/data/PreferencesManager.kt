package com.example.sociallearningapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    companion object {
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch_complete")
    }

    val isFirstLaunchComplete: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[FIRST_LAUNCH_KEY] ?: false
        }

    suspend fun setFirstLaunchComplete() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = true
        }
    }
}