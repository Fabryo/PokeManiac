package com.shodo.android.dashboard.uimodel

import android.net.Uri


data class NewActivityUI(
    val friendName: String,
    val friendImageUrl: String?,
    val date: String,
    val activityType: NewActivityTypeUI,
    val pokemonCard: PokemonCardUI,
    val price: Int
)

enum class NewActivityTypeUI { Purchase, Sale }

data class PokemonCardUI(
    val name: String,
    val imageSource: ImageSourceUI
)

sealed class ImageSourceUI {
    data class UrlSource(val imageUrl: String) : ImageSourceUI()
    data class FileSource(val fileUri: Uri) : ImageSourceUI()
}