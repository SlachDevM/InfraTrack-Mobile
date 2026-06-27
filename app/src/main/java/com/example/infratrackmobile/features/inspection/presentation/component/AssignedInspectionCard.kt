package com.example.infratrackmobile.features.inspection.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infratrackmobile.features.inspection.domain.model.AssignedInspection

@Composable
fun AssignedInspectionCard(
    inspection: AssignedInspection,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${inspection.assetName} (${inspection.assetCode})",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Location: ${inspection.assetLocation}",
                style = MaterialTheme.typography.bodySmall
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Scheduled: ${inspection.scheduledDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Priority: ${inspection.priority}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (inspection.priority == "URGENT") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Reason: ${inspection.businessTriggerReason}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${inspection.status}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
