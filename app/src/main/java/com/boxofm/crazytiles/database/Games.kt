package com.boxofm.crazytiles.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Games(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "games_played")
        val gameCount: Int = 0,

        @ColumnInfo(name = "wins")
        val wins: Int = 0,

        @ColumnInfo(name = "level")
        val level: String = "Easy"
)