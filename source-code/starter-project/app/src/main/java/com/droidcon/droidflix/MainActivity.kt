package com.droidcon.droidflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.droidcon.droidflix.ui.DroidFlixApp
import com.droidcon.droidflix.ui.theme.DroidFlixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DroidFlixTheme {
                DroidFlixApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DroidFlixPreview() {
    DroidFlixTheme {
        DroidFlixApp()
    }
}