package dev.icerock.sample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.icerock.compose.bnn.BottomNestedNavigationItem
import dev.icerock.compose.bnn.NestedNavigationItem
import dev.icerock.sample.app.ui.theme.SampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                val items: List<String> = remember {
                    listOf("First", "Мой вуз", "Third", "Four")
                }
                val nestedItems: List<String> = remember {
                    listOf("Отзывы", "Лента")
                }
                var selectedItem: Int by remember {
                    mutableStateOf(0)
                }
                var nestedSelectedItem: Int? by remember {
                    mutableStateOf(null)
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    bottomBar = {
                        BottomNavigation(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            items.forEachIndexed { index, text ->
                                if (index == 1) {
                                    BottomNestedNavigationItem(
                                        selected = selectedItem == index,
                                        icon = {
                                            Icon(
                                                when (nestedSelectedItem) {
                                                    0 -> Icons.Default.DateRange
                                                    1 -> Icons.Default.DateRange
                                                    else -> Icons.Default.Call
                                                },
                                                contentDescription = null
                                            )
                                        },
                                        nestedItems = nestedItems.mapIndexed { nestedIndex, title ->
                                            NestedNavigationItem(
                                                selected = nestedSelectedItem == nestedIndex,
                                                icon = {
                                                    Icon(
                                                        Icons.Default.DateRange,
                                                        contentDescription = null
                                                    )
                                                },
                                                onClick = {
                                                    nestedSelectedItem = nestedIndex
                                                    selectedItem = index
                                                },
                                                label = {
                                                    Text(title)
                                                }
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = when (nestedSelectedItem) {
                                                    null -> items[index]
                                                    else -> nestedItems[nestedSelectedItem!!]
                                                }
                                            )
                                        }
                                    )
                                } else {
                                    BottomNavigationItem(
                                        selected = selectedItem == index,
                                        onClick = { selectedItem = index },
                                        icon = {
                                            Icon(Icons.Default.Call, contentDescription = null)
                                        },
                                        label = {
                                            Text(text = text)
                                        }
                                    )
                                }
                            }
                        }
                    },
                    content = { padding ->
                        val text: String = when (selectedItem) {
                            1 -> nestedItems[nestedSelectedItem!!]
                            else -> items[selectedItem]
                        }
                        Greeting(
                            modifier = Modifier.padding(padding),
                            name = text
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SampleTheme {
        Greeting("Android")
    }
}