package com.zbd.wearbridge.examples.permlist.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zbd.wearbridge.examples.common.WearAction
import com.zbd.wearbridge.examples.common.WearScreen
import com.zbd.wearbridge.examples.permlist.data.PermissionState

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    WearScreen(
        title = "Perm List",
        available = state.isAvailable, connected = state.isConnected,
        granted = state.permissionState == PermissionState.GRANTED,
        busy = state.isExecuting, logs = state.logs,
        onBind = viewModel::bind, onPermission = viewModel::requestPermission,
        onCheckPermission = viewModel::checkPermission, onUnbind = viewModel::unbind,
        onClearLogs = viewModel::clearLogs,
        actions = listOf(
            WearAction("Capabilities", viewModel::runCapabilities),
            WearAction("Diagnostics", viewModel::runDiagnostics),
            WearAction("Battery dump", viewModel::runDumpsys),
            WearAction("Ping", viewModel::runPing),
        ),
        note = "Diagnostics only. Permission tasks coming later.",
        modifier = modifier,
    )
}
