package com.packt.pets.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.packt.pets.R

/**
 * Created by Tom Buczynski on 15.02.2025.
 */
@Composable
fun PermissionDialog(
    permission: String?,
    rationaleText: String,
    permissionStatus: PermissionStatus,
    modifier: Modifier = Modifier,
    onPermissionAction: (PermissionStatus) -> Unit,
) {
    val context = LocalContext.current

    val currentOnPermissionAction by rememberUpdatedState(onPermissionAction)
    var dialogState by remember(permission) { mutableIntStateOf(0) }

    val permissionDialog = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        currentOnPermissionAction(
            if (isGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED,
        )
    }

    if (dialogState == 1) {
        AlertDialog(
            title = { Text(stringResource(R.string.app_name)) },
            icon = { Icon(Icons.Default.Warning, null) },
            text = { Text(rationaleText) },
            onDismissRequest = { dialogState = 2 },
            confirmButton = {
                TextButton(onClick = {
                    dialogState = 2

                    if (permission == null) {
                        currentOnPermissionAction(PermissionStatus.DENIED)
                        openAppPermissionSettings(context)
                    }
                }) { Text("OK") }
            },
            properties = DialogProperties(dismissOnClickOutside = false),
            modifier = modifier,
        )
    }

    LaunchedEffect(dialogState) {
        if (permission == null) {
            if (dialogState == 0 && permissionStatus == PermissionStatus.UNKNOWN) {
                dialogState = 1
            }
        } else {
            if (dialogState == 0 && permissionStatus == PermissionStatus.UNKNOWN) {
                val actualPermStatus = checkPermissionStatus(context, permission)

                if (actualPermStatus == PermissionStatus.GRANTED) {
                    currentOnPermissionAction(actualPermStatus)
                } else if (actualPermStatus == PermissionStatus.DENIED) {
                    dialogState =
                        if (shouldShowRequestPermissionRationale(context, permission)) 1 else 2
                }
            } else if (dialogState == 2) {
                permissionDialog.launch(permission)
            }
        }
    }
}

enum class PermissionStatus { UNKNOWN, GRANTED, DENIED }

private fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    val activity = context as? Activity ?: return false

    return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
}

private fun checkPermissionStatus(context: Context, permission: String): PermissionStatus {
    return when (ContextCompat.checkSelfPermission(context, permission)) {
        PERMISSION_GRANTED -> PermissionStatus.GRANTED
        PERMISSION_DENIED -> PermissionStatus.DENIED
        else -> PermissionStatus.UNKNOWN
    }
}

private fun openAppPermissionSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }
    context.startActivity(intent)
}