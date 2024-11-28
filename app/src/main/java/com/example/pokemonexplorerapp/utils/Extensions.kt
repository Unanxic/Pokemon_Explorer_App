package com.example.pokemonexplorerapp.utils

fun String.capitalizeFirstLetter(): String {
    return this.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun String.addRouteParams(vararg nameToValue: Pair<String, String>, concatenationCharacter: String = "?"): String {
    val stringBuilder = StringBuilder(this)
    with(stringBuilder) {
        nameToValue.forEach { nameToValuePair ->
            append("$concatenationCharacter${nameToValuePair.first}={${nameToValuePair.second}}")
        }
    }
    return stringBuilder.toString()
}

fun String.addNavigationParams(vararg nameToValue: Pair<String, String>, concatenationCharacter: String = "?"): String {
    val stringBuilder = StringBuilder(this)
    with(stringBuilder) {
        nameToValue.forEach { nameToValuePair ->
            append("$concatenationCharacter${nameToValuePair.first}=${nameToValuePair.second}")
        }
    }
    return stringBuilder.toString()
}