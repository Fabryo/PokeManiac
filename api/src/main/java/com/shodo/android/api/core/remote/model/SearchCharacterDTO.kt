package com.shodo.android.api.core.remote.model

import com.google.gson.annotations.SerializedName

data class SearchCharacterDTO(
    val response: String,
    @SerializedName("results-for")
    val resultFor: String,
    val results: List<SuperheroDTO>
)

data class SuperheroDTO(
    val id: String,
    val name: String,
    val powerstats: PowerStatsDTO,
    val biography: BiographyDTO,
    val appearance: AppearanceDTO,
    val work: WorkDTO,
    val connections: ConnectionsDTO,
    val image: ImageDTO
)

data class PowerStatsDTO(
    val intelligence: String,
    val strength: String,
    val speed: String,
    val durability: String,
    val power: String,
    val combat: String
)

data class BiographyDTO(
    @SerializedName("full-name")
    val fullName: String,
    @SerializedName("alter-egos")
    val alterEgos: String,
    val aliases: List<String>,
    @SerializedName("place-of-birth")
    val placeOfBirth: String,
    @SerializedName("first-appearance")
    val firstAppearance: String,
    val publisher: String,
    val alignment: String
)

data class AppearanceDTO(
    val gender: String,
    val race: String,
    val height: List<String>,
    val weight: List<String>,
    @SerializedName("eye-color")
    val eyeColor: String,
    @SerializedName("hair-color")
    val hairColor: String
)

data class WorkDTO(
    val occupation: String,
    val base: String
)

data class ConnectionsDTO(
    @SerializedName("group-affiliation")
    val groupAffiliation: String,
    val relatives: String
)

data class ImageDTO(
    val url: String
)