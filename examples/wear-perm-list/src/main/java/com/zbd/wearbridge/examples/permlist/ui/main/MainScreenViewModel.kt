package com.zbd.wearbridge.examples.permlist.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zbd.wearbridge.examples.permlist.data.WearBridgeClient
import com.zbd.wearbridge.examples.permlist.data.WearBridgeUiModel
import com.zbd.wearbridge.protocol.WearBridgeContract
import kotlinx.coroutines.flow.StateFlow

/**
 * Planned typed-tasks, pending WearBridge SDK 1.1.0.
 * Manager already runs `pm grant/revoke` + `cmd appops`
 * in its Pro catalog; these IDs are the proposed partner surface.
 */
object PermTasks {
    const val LIST = "privacy.perm.list"
    const val SET = "privacy.perm.set"
    const val APPOPS_GET = "privacy.appops.get"
    const val APPOPS_SET = "privacy.appops.set"

    /** Task IDs are lowercase dot-namespaces, like the server validator requires. */
    fun isValidTaskId(id: String): Boolean =
        id.matches(Regex("[a-z][a-z0-9]*(\\.[a-z][a-z0-9]*)+"))
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

    fun clearLogs() {
        client.clearLogs()
    }

    override fun onCleared() {
        super.onCleared()
        client.destroy()
    }
}
