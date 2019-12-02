package com.boxofm.crazytiles.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface GamesDatabaseDao {

    @Insert
    fun insert(games: Games)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param night new value to write
     */
    @Update
    fun update(games: Games)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from games WHERE level = :level")
    fun get(level: String): Games?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM games")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM games ORDER BY gameId DESC")
    fun getAllGames(): LiveData<List<Games>>

    /**
     * Selects and returns the latest game.
     */
    @Query("SELECT * FROM games ORDER BY gameId DESC LIMIT 1")
    fun getLatestGame(): Games?
}