package com.zbd.wearbridge.examples.dpi.data

import android.content.Context
import android.os.Bundle
import com.zbd.wearbridge.protocol.TaskRequest
import com.zbd.wearbridge.protocol.TaskResult
import com.zbd.wearbridge.protocol.WearBridgeContract
import com.zbd.wearbridge.sdk.WearBridge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class PermissionState {
    UNKNOWN,
    GRANTED,
    DENIED
}

data class WearBridgeUiModel(
    val isAvailable: Boolean = false,
    val isConnected: Boolean = false,
    val permissionState: PermissionState = PermissionState.UNKNOWN,
    val isExecuting: Boolean = false,
    val logs: List<String> = emptyList()
)

class WearBridgeClient(private val context: Context) {

    private val _state = MutableStateFlow(WearBridgeUiModel())
    val state: StateFlow<WearBridgeUiModel> = _state.asStateFlow()

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private val binderReceivedListener = Runnable {
        log("Connected to WearBridge service broker")
        _state.value = _state.value.copy(isConnected = true)
        checkPermission()
    }

    private val binderDeadListener = Runnable {
        log("WearBridge service binder disconnected/died")
        _state.value = _state.value.copy(
            isConnected = false,
            permissionState = PermissionState.UNKNOWN
        )
    }

    private val permissionResultListener = { requestCode: Int, result: Int ->
        val granted = (result == WearBridgeContract.PERMISSION_GRANTED)
        log("Permission result for request #$requestCode: ${if (granted) "GRANTED" else "DENIED ($result)"}")
        _state.value = _state.value.copy(
            permissionState = if (granted) PermissionState.GRANTED else PermissionState.DENIED
        )
    }

    init {
        val available = WearBridge.isAvailable(context)
        log("WearBridge Manager installed: $available")
        _state.value = _state.value.copy(isAvailable = available)

        WearBridge.addBinderReceivedListener(binderReceivedListener)
        WearBridge.addBinderDeadListener(binderDeadListener)
        WearBridge.addPermissionResultListener(permissionResultListener)
    }

    fun bind() {
        val available = WearBridge.isAvailable(context)
        _state.value = _state.value.copy(isAvailable = available)
        if (!available) {
            log("WearBridge manager app (com.zbd.wearbridge) is not installed on this device.")
            return
        }
        log("Binding to WearBridge...")
        WearBridge.bind(context)
    }

    fun unbind() {
        log("Unbinding from WearBridge...")
        WearBridge.unbind()
        _state.value = _state.value.copy(
            isConnected = false,
            permissionState = PermissionState.UNKNOWN
        )
    }

    fun checkPermission() {
        val perm = WearBridge.checkPermission()
        val granted = (perm == WearBridgeContract.PERMISSION_GRANTED)
        log("Checked permission: ${if (granted) "GRANTED" else "DENIED ($perm)"}")
        _state.value = _state.value.copy(
            permissionState = if (granted) PermissionState.GRANTED else PermissionState.DENIED
        )
    }

    fun requestPermission(requestCode: Int = 100) {
        log("Requesting WearBridge permission (code: $requestCode)...")
        WearBridge.requestPermission(requestCode)
    }

    fun executeTask(taskId: String, arguments: Bundle = Bundle.EMPTY) {
        if (!_state.value.isConnected) {
            log("Error: Not connected to WearBridge. Bind first.")
            return
        }
        _state.value = _state.value.copy(isExecuting = true)
        log("Executing task '$taskId'...")
        val request = TaskRequest.create(taskId, arguments)
        WearBridge.execute(request) { result: TaskResult ->
            _state.value = _state.value.copy(isExecuting = false)
            if (result.isSuccess) {
                log("Task '$taskId' SUCCESS in ${result.durationMillis}ms: ${formatBundle(result.data)}")
            } else {
                log("Task '$taskId' FAILED (status: ${result.status}, msg: '${result.message}') in ${result.durationMillis}ms")
            }
        }
    }

    fun clearLogs() {
        _state.value = _state.value.copy(logs = emptyList())
    }

    private fun log(message: String) {
        val timestamp = timeFormat.format(Date())
        val entry = "[$timestamp] $message"
        val currentLogs = _state.value.logs.toMutableList()
        currentLogs.add(0, entry)
        if (currentLogs.size > 100) {
            currentLogs.removeAt(currentLogs.lastIndex)
        }
        _state.value = _state.value.copy(logs = currentLogs)
    }

    @Suppress("DEPRECATION")
    private fun formatBundle(bundle: Bundle?): String {
        if (bundle == null || bundle.isEmpty) return "{}"
        val keys = bundle.keySet()
        val parts = keys.map { key ->
            val value = bundle.get(key)
            "$key=$value"
        }
        return "{ " + parts.joinToString(", ") + " }"
    }

    fun destroy() {
        WearBridge.removeBinderReceivedListener(binderReceivedListener)
        WearBridge.removeBinderDeadListener(binderDeadListener)
        WearBridge.removePermissionResultListener(permissionResultListener)
        WearBridge.unbind()
    }
}
