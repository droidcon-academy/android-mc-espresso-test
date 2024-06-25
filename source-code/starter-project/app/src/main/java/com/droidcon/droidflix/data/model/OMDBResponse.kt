package com.droidcon.droidflix.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OMDBResponse (
    @SerialName("Search")
    val search: List<Flix> = emptyList(),
    @SerialName("Error")
    val error: String? = null,
)