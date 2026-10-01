package com.zbd.wearbridge.examples.animator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.wear.compose.material3.AppScaffold
import androidx.compose.ui.Modifier
import com.zbd.wearbridge.examples.animator.theme.AnimTunerTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      AnimTunerTheme { AppScaffold { com.zbd.wearbridge.examples.animator.ui.main.MainScreen() } }
    }
  }
}
