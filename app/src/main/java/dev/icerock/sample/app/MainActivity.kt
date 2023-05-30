package dev.icerock.sample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import dev.icerock.compose.bnn.PopupBottomNavigationItem
import dev.icerock.compose.bnn.PopupNavigationItem
import dev.icerock.compose.bnn.sample.R
import dev.icerock.sample.app.ui.theme.SampleTheme

@Immutable
sealed interface NavItem

@Immutable
data class ContentNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val badge: Int? = null,
    val content: @Composable (PaddingValues) -> Unit
) : NavItem

@Immutable
data class NestedNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val nestedItems: List<NestedItem>
) : NavItem {

    @Immutable
    data class NestedItem(
        val contentNavItem: ContentNavItem,
        @DrawableRes val selectedIcon: Int
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                var feedCount by remember {
                    mutableStateOf(10)
                }
                var reviewsCount by remember {
                    mutableStateOf(5)
                }
                var notificationsCount by remember {
                    mutableStateOf(3)
                }
                val items: List<NavItem> = remember(feedCount, reviewsCount, notificationsCount) {
                    listOf(
                        ContentNavItem(
                            title = "Расписание",
                            icon = R.drawable.ic_schedule,
                            content = {
                                Text(text = "Привет расписание")
                            }
                        ),
                        ContentNavItem(
                            title = "Объявления",
                            icon = R.drawable.ic_notifications,
                            badge = notificationsCount,
                            content = {
                                notificationsCount = 0
                                Text(text = "Привет объявления")
                            }
                        ),
                        NestedNavItem(
                            title = "Мой вуз",
                            icon = R.drawable.ic_university,
                            nestedItems = listOf(
                                NestedNavItem.NestedItem(
                                    contentNavItem = ContentNavItem(
                                        title = "Отзывы",
                                        icon = R.drawable.ic_reviews,
                                        badge = reviewsCount,
                                        content = {
                                            reviewsCount = 0
                                            Text(text = "Привет отзывы")
                                        }
                                    ),
                                    selectedIcon = R.drawable.ic_university_reviews
                                ),
                                NestedNavItem.NestedItem(
                                    contentNavItem = ContentNavItem(
                                        title = "Лента",
                                        icon = R.drawable.ic_feed,
                                        badge = feedCount,
                                        content = {
                                            feedCount = 0
                                            Text(text = "Привет лента")
                                        }
                                    ),
                                    selectedIcon = R.drawable.ic_university_feed
                                )
                            )
                        ),
                        ContentNavItem(
                            title = "Курсы",
                            icon = R.drawable.ic_courses,
                            content = {
                                Text(text = "Привет курсы")
                            }
                        ),
//                        ContentNavItem(
//                            title = "Настройки",
//                            icon = R.drawable.ic_settings,
//                            content = {
//                                Text(text = "Привет настройки")
//                            }
//                        )
                    )
                }
                val selectedItem: MutableState<Int> = remember {
                    mutableStateOf(items.filterIsInstance<ContentNavItem>().first().icon)
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    bottomBar = {
                        BottomNavigation(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            items.forEach { item ->
                                when (item) {
                                    is ContentNavItem -> {
                                        ContentBottomNavItem(selectedItem, item)
                                    }

                                    is NestedNavItem -> {
                                        NestedBottomNavItem(item, selectedItem)
                                    }
                                }
                            }
                        }
                    },
                    content = { padding ->
                        val item: ContentNavItem = remember(items, selectedItem.value) {
                            items.flatMap { item ->
                                when (item) {
                                    is ContentNavItem -> listOf(item)
                                    is NestedNavItem -> item.nestedItems.map { it.contentNavItem }
                                }
                            }.single { it.icon == selectedItem.value }
                        }
                        item.content(padding)
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.ContentBottomNavItem(
    selectedItem: MutableState<Int>,
    item: ContentNavItem
) {
    // read painter here because icon block of BottomNavigationItem will be recomposed multiple
    // times for animation
    val painter: Painter = painterResource(id = item.icon)

    BottomNavigationItem(
        selected = selectedItem.value == item.icon,
        onClick = { selectedItem.value = item.icon },
        icon = {
            BadgedIcon(painter = painter, badge = item.badge)
        },
        label = {
            Text(text = item.title)
        }
    )
}

@Composable
private fun RowScope.NestedBottomNavItem(
    item: NestedNavItem,
    selectedItem: MutableState<Int>
) {
    var selectedNestedItemId: Int? by remember {
        mutableStateOf(null)
    }
    val nestedContent: List<Int> = remember(item.nestedItems) {
        item.nestedItems.map { it.contentNavItem.icon }
    }

    // read painter here because icon block of BottomNavigationItem will be recomposed multiple
    // times for animation
    val selectedNestedItem: NestedNavItem.NestedItem? =
        remember(selectedNestedItemId, item.nestedItems) {
            if (selectedNestedItemId == null) return@remember null
            item.nestedItems.single { it.contentNavItem.icon == selectedNestedItemId }
        }
    val painter: Painter = painterResource(id = selectedNestedItem?.selectedIcon ?: item.icon)
    val badge: Int? = remember(item.nestedItems) {
        item.nestedItems.sumOf { it.contentNavItem.badge ?: 0 }
            .takeIf { it != 0 }
    }

    PopupBottomNavigationItem(
        selected = nestedContent.contains(selectedItem.value),
        icon = {
            BadgedIcon(painter = painter, badge = badge)
        },
        label = {
            Text(text = selectedNestedItem?.contentNavItem?.title ?: item.title)
        },
        nestedItems = item.nestedItems.map { nestedItem ->
            val contentNestedItem: ContentNavItem = nestedItem.contentNavItem
            PopupNavigationItem(
                selected = selectedItem.value == contentNestedItem.icon || selectedNestedItem == nestedItem,
                onClick = {
                    selectedNestedItemId = nestedItem.contentNavItem.icon
                    selectedItem.value = contentNestedItem.icon
                },
                content = { selected, onClick ->
                    BottomNavigationItem(
                        selected = selected,
                        onClick = onClick,
                        icon = {
                            BadgedIcon(
                                painter = painterResource(id = nestedItem.contentNavItem.icon),
                                badge = nestedItem.contentNavItem.badge
                            )
                        },
                        label = {
                            Text(text = nestedItem.contentNavItem.title)
                        },
                    )
                },
            )
        }
    )
}

@Composable
private fun BadgedIcon(
    painter: Painter,
    badge: Int?
) {
    if (badge != null && badge > 0) {
        BadgedBox(
            badge = {
                Badge { Text(text = badge.toString()) }
            }
        ) {
            Icon(painter = painter, contentDescription = null)
        }
    } else {
        Icon(painter = painter, contentDescription = null)
    }
}
