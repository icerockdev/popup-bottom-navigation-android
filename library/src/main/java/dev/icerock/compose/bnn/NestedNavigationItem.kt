package dev.icerock.compose.bnn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
data class NestedNavigationItem(
    val selected: Boolean,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit,
    val label: @Composable (() -> Unit)? = null,
    val enabled: Boolean = true,
)
