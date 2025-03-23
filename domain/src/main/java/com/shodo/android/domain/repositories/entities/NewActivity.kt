package com.shodo.android.domain.repositories.entities

import java.time.LocalDateTime

data class NewActivity(
    val userName: String,
    val userImageUrl: String?,
    val date: LocalDateTime,
    val pokemonCard: UserPokemonCard,
    val activityType: NewActivityType,
    val price: Int
)

enum class NewActivityType { Purchase, Sale }