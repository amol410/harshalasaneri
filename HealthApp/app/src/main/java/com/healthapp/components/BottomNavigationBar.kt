package com.healthapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomNavigationBar(
    items: List<String>,
    selectedItem: Int,
    onItemClick: (Int) -> Unit
) {
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.CalendarToday,
        Icons.Filled.AccessTime
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item
                    )
                },
                label = { Text(text = item) },
                selected = selectedItem == index,
                onClick = { onItemClick(index) }
            )
        }
    }
}