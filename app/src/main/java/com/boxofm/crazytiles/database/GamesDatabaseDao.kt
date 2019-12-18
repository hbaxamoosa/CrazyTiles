package com.boxofm.crazytiles.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the Games class with Room.
 */
@Dao
interface GamesDatabaseDao {

    @Insert
    fun insert(games: Games)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param games new value to write
     */
    @Update
    fun update(games: Games)

    /**
     * Selects and returns the row that matches the supplied level, which is our key.
     *
     * @param level difficulty of game played
     */
    @Query("SELECT * from games WHERE level = :level")
    suspend fun get(level: String): Games?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM games")
    suspend fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM games ORDER BY games_played DESC")
    fun getAllGames(): List<Games>

    /**
     * Selects and returns the latest game.
     */
    @Query("SELECT * FROM games ORDER BY games_played DESC LIMIT 1")
    suspend fun getLatestGame(): Games?
}