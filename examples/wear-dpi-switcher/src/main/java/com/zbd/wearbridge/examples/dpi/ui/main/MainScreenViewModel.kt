package com.zbd.wearbridge.examples.dpi.ui.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import com.zbd.wearbridge.examples.dpi.data.WearBridgeClient
import com.zbd.wearbridge.examples.dpi.data.WearBridgeUiModel
import com.zbd.wearbridge.protocol.WearBridgeContract
import kotlinx.coroutines.flow.StateFlow

/**
 * Future typed-tasks, pending WearBridge SDK 1.1.0.
 * Manager already runs `wm density` in its Pro catalog
 * (ProToolCatalog.SCREEN_DENSITY); these IDs are the proposed
 * partner surface. Until then calls return TASK_NOT_ALLOWED and
 * the log shows it honestly.
 */
object DpiTasks {
    const val GET = "display.density.get"
    const val SET = "display.density.set"
    const val RESET = "display.density.reset"
    const val ARG_DPI = "dpi"
    val PRESETS = listOf(200, 240, 280, 320)

    /** Mirrors ProToolCatalog.SCREEN_DENSITY bounds (120-640). */
    fun clamp(dpi: Int): Int = dpi.coerceIn(120, 640)
}

class MainScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val client = WearBridgeClient(application.applicationContext)
    val uiState: StateFlow<WearBridgeUiModel> = client.state

    fun bind() {
        client.bind()
    }

    fun unbind() {
        client.unbind()
    }

    fun checkPermission() {
        client.checkPermission()
    }

    fun requestPermission() {
        client.requestPermission()
    }

    fun runPing() {
        client.executeTask(WearBridgeContract.TASK_PING)
    }

    fun runCapabilities() {
        client.executeTask(WearBridgeContract.TASK_CAPABILITIES)
    }

    fun runDiagnostics() {
        client.executeTask(WearBridgeContract.TASK_DIAGNOSTICS)
    }

    fun runDumpsys() {
        client.executeTask(WearBridgeContract.TASK_DUMPSYS)
    }

    fun applyDpiPreset(dpi: Int) {
        val safe = dpi.coerceIn(120, 640)
        val args = Bundle().apply { putInt(DpiTasks.ARG_DPI, safe) }
        client.executeTask(DpiTasks.SET, args)
    }

    fun resetDpi() {
        client.executeTask(DpiTasks.RESET)
    }

    fun clearLogs() {
        client.clearLogs()
    }

    override fun onCleared() {
        super.onCleared()
        client.destroy()
    }
}
