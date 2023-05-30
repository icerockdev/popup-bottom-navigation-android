package dev.icerock.compose.pbn

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
data class PopupNavigationItem(
    val selected: Boolean,
    val onClick: () -> Unit,
    val content: @Composable RowScope.(selected: Boolean, onClick: () -> Unit) -> Unit
)
