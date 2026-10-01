package com.zbd.wearbridge.examples.dpi.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zbd.wearbridge.examples.common.WearAction
import com.zbd.wearbridge.examples.common.WearScreen
import com.zbd.wearbridge.examples.dpi.data.PermissionState

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    WearScreen(
        title = "DPI Switcher",
        available = state.isAvailable, connected = state.isConnected,
        granted = state.permissionState == PermissionState.GRANTED,
        busy = state.isExecuting, logs = state.logs,
        onBind = viewModel::bind, onPermission = viewModel::requestPermission,
        onCheckPermission = viewModel::checkPermission, onUnbind = viewModel::unbind,
        onClearLogs = viewModel::clearLogs,
        actions = DpiTasks.PRESETS.map { dpi -> WearAction("$dpi DPI") { viewModel.applyDpiPreset(dpi) } } +
            listOf(WearAction("Reset density", viewModel::resetDpi), WearAction("Capabilities", viewModel::runCapabilities)),
        note = "Approve each change in WearBridge. Shell server must be running.",
        modifier = modifier,
    )
}
