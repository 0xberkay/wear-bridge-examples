package com.zbd.wearbridge.examples.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnItemScope
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight

data class WearAction(val label: String, val onClick: () -> Unit)

/** Shared wrist-sized layout compiled into each standalone example APK. */
@Composable
fun WearScreen(
    title: String,
    available: Boolean,
    connected: Boolean,
    granted: Boolean,
    busy: Boolean,
    logs: List<String>,
    onBind: () -> Unit,
    onPermission: () -> Unit,
    onCheckPermission: () -> Unit,
    onUnbind: () -> Unit,
    onClearLogs: () -> Unit,
    actions: List<WearAction>,
    note: String? = null,
    modifier: Modifier = Modifier,
) {
    val listState = rememberTransformingLazyColumnState()
    ScreenScaffold(
        scrollState = listState,
        modifier = modifier.fillMaxSize().background(Color.Black),
    ) { scaffoldPadding ->
        TransformingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = maxOf(16.dp, scaffoldPadding.calculateLeftPadding(LayoutDirection.Ltr)),
                end = maxOf(16.dp, scaffoldPadding.calculateRightPadding(LayoutDirection.Ltr)),
                top = maxOf(36.dp, scaffoldPadding.calculateTopPadding()),
                bottom = maxOf(40.dp, scaffoldPadding.calculateBottomPadding()),
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item { ListHeader { Text(title, textAlign = TextAlign.Center) } }
            item {
                Text(
                    text = when {
                        !available -> "Install WearBridge Manager"
                        !connected -> "WearBridge disconnected"
                        !granted -> "Connected · permission needed"
                        busy -> "Running…"
                        else -> "WearBridge ready"
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
            if (!connected) {
                item { ActionButton("Connect", available && !busy, onBind) }
            } else if (!granted) {
                item { ActionButton("Grant access", !busy, onPermission) }
                item { ActionButton("Check access", !busy, onCheckPermission, secondary = true) }
            }
            if (note != null) {
                item {
                    Text(note, Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                }
            }
            actions.forEach { action ->
                item { ActionButton(action.label, connected && granted && !busy, action.onClick) }
            }
            if (logs.isNotEmpty()) {
                item { ListHeader { Text("Results") } }
                // Short, individually scrollable rows: never one screen-sized phone card.
                logs.take(12).forEach { log ->
                    log.chunked(100).forEach { line ->
                        item {
                            Text(line, Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center)
                        }
                    }
                }
                item { ActionButton("Clear results", !busy, onClearLogs, secondary = true) }
            }
            if (connected) {
                item { ActionButton("Disconnect", !busy, onUnbind, secondary = true) }
            }
        }
    }
}

@Composable
private fun TransformingLazyColumnItemScope.ActionButton(label: String, enabled: Boolean, onClick: () -> Unit, secondary: Boolean = false) {
    val transformationSpec = rememberTransformationSpec()
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
        transformation = SurfaceTransformation(transformationSpec),
        colors = if (secondary) ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) else ButtonDefaults.buttonColors(),
    ) { Text(label, textAlign = TextAlign.Center) }
}
