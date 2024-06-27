package com.droidcon.droidflix.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher

fun containsStringCaseInsensitive(value: String): SemanticsMatcher {
    return SemanticsMatcher("Contains $value case insensitive") { semanticsNode ->
        val text = semanticsNode.config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text
        text?.contains(value, true) == true
    }
}