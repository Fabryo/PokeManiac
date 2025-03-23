package com.shodo.android.database.friends

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

@Dao
interface FriendsDao {

    @Query("SELECT * FROM friends_table")
    fun getAllFriends(): List<FriendBase>?

    @Insert(onConflict = REPLACE)
    fun insertAllCharacters(characters: List<FriendBase>)

    @Query("SELECT * FROM friends_table WHERE name = :friendName")
    fun getFriendByName(friendName: String): FriendBase?

    @Query("SELECT * FROM friends_table WHERE id = :friendId")
    fun getFriendById(friendId: String): FriendBase?

    @Query("UPDATE friends_table SET isSubscribed=:isSubscribed WHERE id = :id")
    fun updateSubscribedCharacter(id: Int, isSubscribed: Boolean)

    @Query("SELECT * FROM friends_table where isSubscribed = 1")
    fun getSubscribedCharacters(): List<FriendBase>

    @Insert(onConflict = REPLACE)
    fun insert(character: FriendBase)

    @Delete
    fun delete(character: FriendBase)

    @Query("DELETE FROM friends_table")
    fun deleteAllCharacters()

    @Query("DELETE FROM friends_table WHERE id = :friendId")
    fun deleteFriend(friendId: String)
}