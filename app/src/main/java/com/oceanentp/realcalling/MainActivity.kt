package com.oceanentp.realcalling

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oceanentp.realcalling.ui.theme.RealCallingTheme
import com.oceanentp.realcalling.util.PermissionChecker

class MainActivity : ComponentActivity() {

    private val requiredPermissions = arrayOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_PHONE_STATE
    )

    // Observable states so Compose recomposes automatically when permissions change
    private var allPermissionsGranted by mutableStateOf(false)
    private var showPermissionRationale by mutableStateOf(false)

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            allPermissionsGranted = results.values.all { it }
            if (!allPermissionsGranted) {
                // Check if any denied permission should show rationale for a follow-up request
                showPermissionRationale = requiredPermissions.any { permission ->
                    shouldShowRequestPermissionRationale(permission)
                }
            } else {
                showPermissionRationale = false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize observable state before Compose reads it
        allPermissionsGranted = PermissionChecker.hasAllPermission(this)

        setContent {
            RealCallingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (allPermissionsGranted) {
                        // TODO: Replace with MainScreen composable once built
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "All permissions granted. App is ready.")
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (showPermissionRationale) {
                                Text(
                                    text = "This app needs Phone, Contacts, and Call Log " +
                                            "permissions to place calls and display your call history."
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            } else {
                                Text(text = "Permissions are required to use this app.")
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            Button(onClick = { launchPermissionRequest() }) {
                                Text("Grant Permissions")
                            }
                        }
                    }
                }
            }
        }

        // Request permissions immediately on first launch if not already granted
        if (!allPermissionsGranted) {
            checkAndRequestPermissions()
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-evaluate in case the user granted permissions via the system Settings
        val granted = PermissionChecker.hasAllPermission(this)
        if (granted) {
            allPermissionsGranted = true
            showPermissionRationale = false
        }
    }

    /**
     * Shows the rationale UI when the system recommends it; otherwise launches the system
     * permission dialog immediately.
     */
    private fun checkAndRequestPermissions() {
        val shouldShowRationale = requiredPermissions.any { permission ->
            shouldShowRequestPermissionRationale(permission)
        }
        if (shouldShowRationale) {
            showPermissionRationale = true
        } else {
            launchPermissionRequest()
        }
    }

    private fun launchPermissionRequest() {
        showPermissionRationale = false
        permissionLauncher.launch(requiredPermissions)
    }
}

