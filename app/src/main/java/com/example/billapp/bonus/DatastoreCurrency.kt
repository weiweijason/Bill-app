package com.example.billapp.bonus

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "exchange_rates")

object DataStoreManager {
    private val EXCHANGE_RATE_PREFIX = "exchange_rate_"
    private val LAST_UPDATE_TIME_KEY = longPreferencesKey("last_update_time")

    fun getExchangeRate(context: Context, currency: String): Flow<Double?> {
        val key = doublePreferencesKey(EXCHANGE_RATE_PREFIX + currency)
        return context.dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    suspend fun saveExchangeRate(context: Context, currency: String, rate: Double) {
        val key = doublePreferencesKey(EXCHANGE_RATE_PREFIX + currency)
        context.dataStore.edit { preferences ->
            preferences[key] = rate
        }
    }

    fun getLastUpdateTime(context: Context): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[LAST_UPDATE_TIME_KEY]
        }
    }

    suspend fun saveLastUpdateTime(context: Context, time: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_UPDATE_TIME_KEY] = time
        }
    }
}