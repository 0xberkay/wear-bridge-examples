package com.zbd.wearbridge.examples.permlist.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zbd.wearbridge.examples.permlist.data.PermissionState
import com.zbd.wearbridge.examples.permlist.data.WearBridgeUiModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreenContent(
        state = state,
        onBind = viewModel::bind,
        onUnbind = viewModel::unbind,
        onCheckPermission = viewModel::checkPermission,
        onRequestPermission = viewModel::requestPermission,
        onPing = viewModel::runPing,
        onCapabilities = viewModel::runCapabilities,
        onDiagnostics = viewModel::runDiagnostics,
        onDumpsys = viewModel::runDumpsys,
        onClearLogs = viewModel::clearLogs,
        modifier = modifier
    )
}

@Composable
fun MainScreenContent(
    state: WearBridgeUiModel,
    onBind: () -> Unit,
    onUnbind: () -> Unit,
    onCheckPermission: () -> Unit,
    onRequestPermission: () -> Unit,
    onPing: () -> Unit,
    onCapabilities: () -> Unit,
    onDiagnostics: () -> Unit,
    onDumpsys: () -> Unit,
    onClearLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Wear Perm List",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "App permission viewer (SDK 1.1.0: privacy.perm.*)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            StatusCard(state = state)
        }

        item {
            ConnectionSection(
                isConnected = state.isConnected,
                onBind = onBind,
                onUnbind = onUnbind,
                onCheckPermission = onCheckPermission,
                onRequestPermission = onRequestPermission
            )
        }

        item {
            TaskSection(
                isConnected = state.isConnected,
                isExecuting = state.isExecuting,
                onPing = onPing,
                onCapabilities = onCapabilities,
                onDiagnostics = onDiagnostics,
                onDumpsys = onDumpsys
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Event Logs (${state.logs.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedButton(
                    onClick = onClearLogs,
                    enabled = state.logs.isNotEmpty()
                ) {
                    Text("Clear", fontSize = 12.sp)
                }
            }
        }

        if (state.logs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = "No logs yet. Press 'Bind' to start.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(state.logs) { log ->
                LogItem(log = log)
            }
        }
    }
}

@Composable
private fun StatusCard(state: WearBridgeUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusRow(
                label = "Manager APK",
                value = if (state.isAvailable) "Installed" else "Not Installed",
                active = state.isAvailable
            )
            StatusRow(
                label = "Service State",
                value = if (state.isConnected) "Connected" else "Disconnected",
                active = state.isConnected
            )
            StatusRow(
                label = "Permission",
                value = when (state.permissionState) {
                    PermissionState.GRANTED -> "Granted"
                    PermissionState.DENIED -> "Denied"
                    PermissionState.UNKNOWN -> "Unknown"
                },
                active = state.permissionState == PermissionState.GRANTED
            )
        }
    }
}

@Composable
private fun StatusRow(label: String, value: String, active: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (active) Color(0xFF4CAF50) else Color(0xFFE57373),
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ConnectionSection(
    isConnected: Boolean,
    onBind: () -> Unit,
    onUnbind: () -> Unit,
    onCheckPermission: () -> Unit,
    onRequestPermission: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Broker Actions", style = MaterialTheme.typography.titleSmall)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onBind,
                    enabled = !isConnected,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Bind")
                }
                OutlinedButton(
                    onClick = onUnbind,
                    enabled = isConnected,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Unbind")
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onCheckPermission,
                    enabled = isConnected,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Check Perm", fontSize = 12.sp)
                }
                Button(
                    onClick = onRequestPermission,
                    enabled = isConnected,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Request Perm", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun TaskSection(
    isConnected: Boolean,
    isExecuting: Boolean,
    onPing: () -> Unit,
    onCapabilities: () -> Unit,
    onDiagnostics: () -> Unit,
    onDumpsys: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Task Catalog", style = MaterialTheme.typography.titleSmall)
                if (isExecuting) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onPing,
                    enabled = isConnected && !isExecuting,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ping")
                }
                Button(
                    onClick = onCapabilities,
                    enabled = isConnected && !isExecuting,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Capabilities")
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onDiagnostics,
                    enabled = isConnected && !isExecuting,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Diagnostics")
                }
                Button(
                    onClick = onDumpsys,
                    enabled = isConnected && !isExecuting,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Dumpsys")
                }
            }
        }
    }
}

@Composable
private fun LogItem(log: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = log,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
