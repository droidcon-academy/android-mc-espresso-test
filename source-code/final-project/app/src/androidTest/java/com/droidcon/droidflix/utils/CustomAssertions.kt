package com.droidcon.droidflix.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

fun SemanticsNodeInteraction.assertContainsIgnoreCase(value: String): SemanticsNodeInteraction {
    return assert(SemanticsMatcher("Contains $value, case insensitive") { semanticsNode ->
        val text = semanticsNode.config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text
        text?.contains(value, true) == true
    })
}