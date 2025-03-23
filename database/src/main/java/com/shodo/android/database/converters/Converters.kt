package com.shodo.android.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shodo.android.database.friends.FriendBase
import com.shodo.android.database.myactivities.MyActivityBase

class Converters {

    @TypeConverter
    fun fromPokemonCardList(value: List<FriendBase.PokemonCardBase>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toPokemonCardList(value: String): List<FriendBase.PokemonCardBase> {
        val listType = object : TypeToken<List<FriendBase.PokemonCardBase>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromMyPokemonCard(value: MyActivityBase.MyPokemonCardBase): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toMyPokemonCard(value: String): MyActivityBase.MyPokemonCardBase {
        val type = object : TypeToken<MyActivityBase.MyPokemonCardBase>() {}.type
        return Gson().fromJson(value, type)
    }
}