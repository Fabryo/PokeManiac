package com.shodo.android.myprofile.uimodel

import android.net.Uri

data class MyProfileUI(
    val name: String?,
    val imageUrl: String?,
    val pokemonCards: List<MyProfilePokemonCardUI>
)

data class MyProfilePokemonCardUI(
    val name: String,
    val imageUri: Uri,
    val totalVotes: Int
)