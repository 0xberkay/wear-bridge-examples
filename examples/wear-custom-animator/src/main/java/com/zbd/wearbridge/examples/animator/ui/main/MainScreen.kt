package com.zbd.wearbridge.examples.animator.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zbd.wearbridge.examples.common.WearAction
import com.zbd.wearbridge.examples.common.WearScreen
import com.zbd.wearbridge.examples.animator.data.PermissionState

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    WearScreen(
        title = "Custom Animator",
        available = state.isAvailable, connected = state.isConnected,
        granted = state.permissionState == PermissionState.GRANTED,
        busy = state.isExecuting, logs = state.logs,
        onBind = viewModel::bind, onPermission = viewModel::requestPermission,
        onCheckPermission = viewModel::checkPermission, onUnbind = viewModel::unbind,
        onClearLogs = viewModel::clearLogs,
        actions = listOf(
            WearAction("Animations off") { viewModel.applyAnimScale(0.0) },
            WearAction("Fast · 0.5×") { viewModel.applyAnimScale(0.5) },
            WearAction("Normal · 1×") { viewModel.applyAnimScale(1.0) },
            WearAction("Slow · 2×") { viewModel.applyAnimScale(2.0) },
            WearAction("Capabilities", viewModel::runCapabilities),
        ),
        note = "Approve each change in WearBridge. Shell server must be running.",
        modifier = modifier,
    )
}
