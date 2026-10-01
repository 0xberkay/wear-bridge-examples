package com.zbd.wearbridge.examples.animator.ui.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import com.zbd.wearbridge.examples.animator.data.WearBridgeClient
import com.zbd.wearbridge.examples.animator.data.WearBridgeUiModel
import com.zbd.wearbridge.protocol.WearBridgeContract
import kotlinx.coroutines.flow.StateFlow

/**
 * Future typed-tasks, pending WearBridge SDK 1.1.0.
 * Manager already runs `settings put global *_animation_scale`
 * in its Pro catalog (ProToolCatalog.ANIM_SCALE); these IDs are
 * the proposed partner surface. Until then calls return
 * TASK_NOT_ALLOWED and the log shows it honestly.
 */
object AnimTasks {
    const val SET = "ui.anim.set"
    const val GET = "ui.anim.get"
    const val ARG_SCALE = "scale"
    val PRESETS = listOf(0.0, 0.5, 1.0, 2.0)

    /** Mirrors ProToolCatalog.ANIM_SCALE bounds (0-10, 0 = off). */
    fun clamp(scale: Double): Double = scale.coerceIn(0.0, 10.0)
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

    fun applyAnimScale(scale: Double) {
        val safe = scale.coerceIn(0.0, 10.0)
        val args = Bundle().apply { putDouble(AnimTasks.ARG_SCALE, safe) }
        client.executeTask(AnimTasks.SET, args)
    }

    fun clearLogs() {
        client.clearLogs()
    }

    override fun onCleared() {
        super.onCleared()
        client.destroy()
    }
}
