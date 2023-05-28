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
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import dev.icerock.compose.bnn.BottomNestedNavigationItem
import dev.icerock.compose.bnn.NestedNavigationItem
import dev.icerock.compose.bnn.sample.R
import dev.icerock.sample.app.ui.theme.SampleTheme

@Immutable
sealed interface NavItem

@Immutable
data class ContentNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val content: @Composable (PaddingValues) -> Unit
) : NavItem

@Immutable
data class NestedNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val nestedItems: List<ContentNavItem>
) : NavItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                val items: List<NavItem> = remember {
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
                            content = {
                                Text(text = "Привет объявления")
                            }
                        ),
                        NestedNavItem(
                            title = "Мой вуз",
                            icon = R.drawable.ic_university,
                            nestedItems = listOf(
                                ContentNavItem(
                                    title = "Отзывы",
                                    icon = R.drawable.ic_reviews,
                                    content = {
                                        Text(text = "Привет отзывы")
                                    }
                                ),
                                ContentNavItem(
                                    title = "Лента",
                                    icon = R.drawable.ic_feed,
                                    content = {
                                        Text(text = "Привет лента")
                                    }
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
                        ContentNavItem(
                            title = "Настройки",
                            icon = R.drawable.ic_settings,
                            content = {
                                Text(text = "Привет настройки")
                            }
                        )
                    )
                }
                val selectedItem: MutableState<ContentNavItem> = remember {
                    mutableStateOf(items.filterIsInstance<ContentNavItem>().first())
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
                        selectedItem.value.content(padding)
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.ContentBottomNavItem(
    selectedItem: MutableState<ContentNavItem>,
    item: ContentNavItem
) {
    BottomNavigationItem(
        selected = selectedItem.value == item,
        onClick = { selectedItem.value = item },
        icon = {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null
            )
        },
        label = {
            Text(text = item.title)
        }
    )
}

@Composable
private fun RowScope.NestedBottomNavItem(
    item: NestedNavItem,
    selectedItem: MutableState<ContentNavItem>
) {
    BottomNestedNavigationItem(
        selected = item.nestedItems.contains(selectedItem.value),
        icon = {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null
            )
        },
        nestedItems = item.nestedItems.map { nestedItem ->
            NestedNavigationItem(
                selected = selectedItem.value == nestedItem,
                icon = {
                    Icon(
                        painter = painterResource(id = nestedItem.icon),
                        contentDescription = null
                    )
                },
                onClick = { selectedItem.value = nestedItem },
                label = {
                    Text(nestedItem.title)
                }
            )
        },
        label = {
            Text(text = item.title)
        }
    )
}
