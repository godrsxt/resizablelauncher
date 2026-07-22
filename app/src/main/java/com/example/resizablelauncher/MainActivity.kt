package com.example.resizablelauncher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val installedApps = loadInstalledApps()

        setContent {
            MaterialTheme {
                val isSecondary = isSecondaryDisplay()
                LauncherScreen(installedApps = installedApps, isResizable = isSecondary)
            }
        }
    }

    private fun loadInstalledApps(): List<AppInfo> {
        val pm: PackageManager = packageManager
        val apps = mutableListOf<AppInfo>()

        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfos = pm.queryIntentActivities(intent, 0)

        for (resolveInfo in resolveInfos) {
            val packageName = resolveInfo.activityInfo.packageName
            if (packageName == this.packageName) continue

            val appInfo = pm.getApplicationInfo(packageName, 0)
            val label = pm.getApplicationLabel(appInfo).toString()
            val icon = pm.getApplicationIcon(appInfo)

            apps.add(AppInfo(label, packageName, icon))
        }
        return apps.sortedBy { it.label.lowercase() }
    }

    private fun isSecondaryDisplay(): Boolean {
        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as android.hardware.display.DisplayManager
        val displays = displayManager.getDisplays(android.hardware.display.DisplayManager.DISPLAY_CATEGORY_PRESENTATION)
        return displays.isNotEmpty()
    }
}

data class AppInfo(
    val label: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable
)

@Composable
fun LauncherScreen(installedApps: List<AppInfo>, isResizable: Boolean) {
    val context = LocalContext.current
    val scaleFactor = if (isResizable) 0.9f else 1f

    Column(modifier = Modifier.fillMaxSize().padding((16 * scaleFactor).dp)) {
        Text(
            text = if (isResizable) "Resizable Cast Launcher" else "ResizableLauncher",
            style = MaterialTheme.typography.headlineLarge
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(installedApps) { app ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        val intent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                        context.startActivity(intent)
                    }
                ) {
                    Image(
                        bitmap = app.icon.toBitmap().asImageBitmap(),
                        contentDescription = app.label,
                        modifier = Modifier.size((64 * scaleFactor).dp)
                    )
                    Text(text = app.label, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}