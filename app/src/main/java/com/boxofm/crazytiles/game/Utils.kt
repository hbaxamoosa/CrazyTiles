package com.boxofm.crazytiles.game

import com.boxofm.crazytiles.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import timber.log.Timber

object Utils {
    var cache: Long = 3600

    fun getCacheExpiration(): Long {
        cache = if (BuildConfig.DEBUG)
            2
        else 3600
        return cache
    }

    fun fetchRemoteConfigValues(remoteConfig: FirebaseRemoteConfig, difficultyLevel: String): Long {
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Timber.v("Config params updated: $updated")
                        /*Timber.v("Fetch and activate succeeded")
                        Timber.v("%s %s", "value of difficulty_level_easy is ", remoteConfig.getLong("difficulty_level_easy"))
                        Timber.v("%s %s", "value of difficulty_level_medium is ", remoteConfig.getLong("difficulty_level_medium"))
                        Timber.v("%s %s", "value of difficulty_level_hard is ", remoteConfig.getLong("difficulty_level_hard"))*/

                    } else {
                        Timber.v("Fetch and activate failed")
                    }
                }
        return when (difficultyLevel) { // Set the time for the game
            "Easy" -> {
                /*Timber.v("%s %s", "value of difficulty_level_easy is ", remoteConfig.getLong("difficulty_level_easy"))*/
                remoteConfig.getLong("difficulty_level_easy")
            }
            "Medium" -> {
                /*Timber.v("%s %s", "value of difficulty_level_medium is ", remoteConfig.getLong("difficulty_level_medium"))*/
                remoteConfig.getLong("difficulty_level_medium")
            }
            "Hard" -> {
                /*Timber.v("%s %s", "value of difficulty_level_hard is ", remoteConfig.getLong("difficulty_level_hard"))*/
                remoteConfig.getLong("difficulty_level_hard")
            }
            else -> 5000L
        }
    }
}