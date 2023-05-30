package dev.icerock.compose.bnn

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val popupHeightInDp = 56
private const val popupWidthPerItemInDp = 80
private const val popupSpacingFromNavigationInDp = 8

@Composable
fun RowScope.PopupBottomNavigationItem(
    selected: Boolean,
    icon: @Composable () -> Unit,
    nestedItems: List<PopupNavigationItem>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium),
    popupBackgroundColor: Color = MaterialTheme.colors.primarySurface,
    popupContentColor: Color = contentColorFor(popupBackgroundColor),
    popupElevation: Dp = BottomNavigationDefaults.Elevation,
) {
    var nestedSelectionShow: Boolean by remember { mutableStateOf(false) }
    var allowedShow: Boolean by remember { mutableStateOf(true) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    BottomNavigationItem(
        modifier = modifier,
        enabled = enabled,
        alwaysShowLabel = alwaysShowLabel,
        interactionSource = interactionSource,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        selected = selected,
        onClick = {
            if (allowedShow) nestedSelectionShow = true
            val selectedItem: PopupNavigationItem? = nestedItems.singleOrNull { it.selected }
            selectedItem?.onClick?.invoke()
        },
        label = label,
        icon = {
            Box(modifier = Modifier.fillMaxHeight()) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    icon()
                }

                if (nestedSelectionShow) {
                    val density: Density = LocalDensity.current
                    val offset: IntOffset = remember(density) {
                        val yOffsetInDp: Int = popupSpacingFromNavigationInDp + popupHeightInDp
                        IntOffset(x = 0, y = -(yOffsetInDp * density.density).toInt())
                    }
                    Popup(
                        alignment = Alignment.TopCenter,
                        offset = offset,
                        onDismissRequest = {
                            allowedShow = false
                            nestedSelectionShow = false
                            coroutineScope.launch {
                                delay(100)
                                allowedShow = true
                            }
                        }
                    ) {
                        PopupNavigation(
                            nestedIcons = nestedItems,
                            dismissPopup = { nestedSelectionShow = false },
                            backgroundColor = popupBackgroundColor,
                            contentColor = popupContentColor,
                            elevation = popupElevation
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun PopupNavigation(
    nestedIcons: List<PopupNavigationItem>,
    dismissPopup: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    elevation: Dp
) {
    Surface(
        modifier = modifier
            .height(popupHeightInDp.dp)
            .width((nestedIcons.size * popupWidthPerItemInDp).dp),
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = MaterialTheme.shapes.medium
    ) {
        Row {
            nestedIcons.forEach { nestedIcon ->
                nestedIcon.content(this, nestedIcon.selected) {
                    dismissPopup()
                    nestedIcon.onClick()
                }
            }
        }
    }
}
