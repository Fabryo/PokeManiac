package com.shodo.android.myprofile.uimodel

import android.net.Uri
import kotlinx.collections.immutable.PersistentList

data class MyProfileUI(
    val name: String?,
    val imageUrl: String?,
    val pokemonCards: PersistentList<MyProfilePokemonCardUI>
)

data class MyProfilePokemonCardUI(
    val id: String,
    val name: String,
    val imageUri: Uri,
    val totalVotes: Int
)