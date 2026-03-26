package com.oceanentp.realcalling.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

private enum class AppScreen { DIALER, CALL_LOGS, CONTACTS }

@Composable
fun MainScreen(onCallNumber: (String) -> Unit) {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.DIALER) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.DIALER,
                    onClick = { currentScreen = AppScreen.DIALER },
                    icon = { Icon(Icons.Default.Phone, contentDescription = "Dialer") },
                    label = { Text("Dialer") }
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.CALL_LOGS,
                    onClick = { currentScreen = AppScreen.CALL_LOGS },
                    icon = { Icon(Icons.Default.List, contentDescription = "Recents") },
                    label = { Text("Recents") }
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.CONTACTS,
                    onClick = { currentScreen = AppScreen.CONTACTS },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Contacts") },
                    label = { Text("Contacts") }
                )
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            AppScreen.DIALER -> DialerScreen(
                modifier = Modifier.padding(innerPadding),
                onCallNumber = onCallNumber
            )
            AppScreen.CALL_LOGS -> CallLogsScreen(
                modifier = Modifier.padding(innerPadding),
                onCallNumber = onCallNumber
            )
            AppScreen.CONTACTS -> ContactsScreen(
                modifier = Modifier.padding(innerPadding),
                onCallNumber = onCallNumber
            )
        }
    }
}
