package com.droidcon.droidflix.data.model

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class Flix (
    @SerialName("imdbID")
    val id: String = "n/a",
    @SerialName("Title")
    val title: String = "n/a",
    @SerialName("Year")
    val year: String = "n/a",
    @SerialName("Plot")
    val plot: String = "n/a",
    @SerialName("Poster")
    val poster: String?,
    @SerialName("Ratings")
    val ratings: List<Rating> = emptyList(),
    @SerialName("Error")
    val error: String? = null,
) : Parcelable

@Serializable
@Parcelize
data class Rating(
    @SerialName("Source")
    val source: String = "n/a",
    @SerialName("Value")
    val value: String = "n/a"
) : Parcelable {
    override fun toString(): String {
        return "$source: $value"
    }
}