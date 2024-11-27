package com.example.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "types")
    val types: List<TypeDetail>,
    @Json(name = "sprites")
    val sprites: Sprites,
    @Json(name = "stats")
    val stats: List<Stat>,
    @Json(name = "abilities")
    val abilities: List<Ability>
)

@JsonClass(generateAdapter = true)
data class TypeDetail(
    @Json(name = "type")
    val type: TypeName
)

@JsonClass(generateAdapter = true)
data class TypeName(
    @Json(name = "name")
    val name: String
)

@JsonClass(generateAdapter = true)
data class Sprites(
    @Json(name = "front_default")
    val frontDefault: String?
)

@JsonClass(generateAdapter = true)
data class Stat(
    @Json(name = "base_stat")
    val baseStat: Int,
    @Json(name = "stat")
    val stat: StatDetail
)

@JsonClass(generateAdapter = true)
data class StatDetail(
    @Json(name = "name")
    val name: String
)

@JsonClass(generateAdapter = true)
data class Ability(
    @Json(name = "ability")
    val ability: AbilityDetail
)

@JsonClass(generateAdapter = true)
data class AbilityDetail(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)
