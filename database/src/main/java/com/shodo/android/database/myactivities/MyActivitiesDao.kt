package com.shodo.android.database.myactivities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

@Dao
interface MyActivitiesDao {

    @Query("SELECT * FROM my_activities_table")
    fun getAllActivities(): List<MyActivityBase>?

    @Insert(onConflict = REPLACE)
    fun insertAllCharacters(myActivities: List<MyActivityBase>)

    @Insert(onConflict = REPLACE)
    fun insert(myActivity: MyActivityBase)

    @Delete
    fun delete(myActivity: MyActivityBase)

    @Query("DELETE FROM my_activities_table")
    fun deleteAllMyActivities()
}