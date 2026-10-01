package com.zbd.wearbridge.examples.dpi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.wear.compose.material3.AppScaffold
import androidx.compose.ui.Modifier
import com.zbd.wearbridge.examples.dpi.theme.DpiSwitcherTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      DpiSwitcherTheme { AppScaffold { com.zbd.wearbridge.examples.dpi.ui.main.MainScreen() } }
    }
  }
}
