package com.oceanentp.realcalling.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Call type constants matching android.provider.CallLog.Calls
private const val CALL_TYPE_INCOMING = 1
private const val CALL_TYPE_OUTGOING = 2
private const val CALL_TYPE_MISSED = 3

private data class MockCallLog(
    val name: String?,
    val number: String,
    val type: Int,
    val dateMs: Long,
    val durationSec: Long
)

private val mockCallLogs: List<MockCallLog> = run {
    val now = System.currentTimeMillis()
    listOf(
        MockCallLog("Alice Johnson", "+1-555-0101", CALL_TYPE_INCOMING, now - 1_800_000L, 145),
        MockCallLog("Bob Smith", "+1-555-0202", CALL_TYPE_OUTGOING, now - 7_200_000L, 320),
        MockCallLog(null, "+1-555-0303", CALL_TYPE_MISSED, now - 10_800_000L, 0),
        MockCallLog("Carol White", "+1-555-0404", CALL_TYPE_INCOMING, now - 86_400_000L, 62),
        MockCallLog("Dave Brown", "+1-555-0505", CALL_TYPE_OUTGOING, now - 172_800_000L, 531),
        MockCallLog(null, "+1-555-0606", CALL_TYPE_MISSED, now - 259_200_000L, 0),
        MockCallLog("Eve Davis", "+1-555-0707", CALL_TYPE_INCOMING, now - 345_600_000L, 87),
        MockCallLog("Frank Miller", "+1-555-0808", CALL_TYPE_OUTGOING, now - 432_000_000L, 210),
    )
}

@Composable
fun CallLogsScreen(
    modifier: Modifier = Modifier,
    onCallNumber: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Recents",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        HorizontalDivider()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(mockCallLogs) { log ->
                CallLogItem(log = log, onCallNumber = onCallNumber)
                HorizontalDivider(modifier = Modifier.padding(start = 72.dp))
            }
        }
    }
}

@Composable
private fun CallLogItem(log: MockCallLog, onCallNumber: (String) -> Unit) {
    val colorIncoming = Color(0xFF4CAF50)
    val colorMissed = Color(0xFFF44336)

    val (typeIcon, typeColor, typeLabel) = when (log.type) {
        CALL_TYPE_INCOMING -> Triple(Icons.Default.KeyboardArrowDown, colorIncoming, "Incoming")
        CALL_TYPE_OUTGOING -> Triple(Icons.Default.KeyboardArrowUp, MaterialTheme.colorScheme.primary, "Outgoing")
        else -> Triple(Icons.Default.Phone, colorMissed, "Missed")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCallNumber(log.number) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = typeColor.copy(alpha = 0.12f),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = typeIcon,
                    contentDescription = typeLabel,
                    tint = typeColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = log.name ?: log.number,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (log.type == CALL_TYPE_MISSED) {
                    colorMissed
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$typeLabel · ${formatCallDate(log.dateMs)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (log.durationSec > 0) {
                    Text(
                        text = " · ${formatCallDuration(log.durationSec)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (log.name != null) {
                Text(
                    text = log.number,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        IconButton(onClick = { onCallNumber(log.number) }) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Call ${log.name ?: log.number}",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun formatCallDate(dateMs: Long): String {
    val diffMs = System.currentTimeMillis() - dateMs
    return when {
        diffMs < 86_400_000L -> SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(dateMs))
        diffMs < 604_800_000L -> SimpleDateFormat("EEE", Locale.getDefault()).format(Date(dateMs))
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(dateMs))
    }
}

private fun formatCallDuration(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return if (minutes > 0) "${minutes}m ${secs}s" else "${secs}s"
}
