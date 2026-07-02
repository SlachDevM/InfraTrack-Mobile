package com.example.infratrackmobile.features.workorder.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.infratrackmobile.core.ui.util.DisplayFormatter
import com.example.infratrackmobile.features.workorder.domain.model.AssignedWorkOrder

@Composable
fun WorkOrderCard(
    workOrder: AssignedWorkOrder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = workOrder.assetName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = workOrder.status)
            }

            Text(
                text = workOrder.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Assigned",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = DisplayFormatter.formatDate(workOrder.assignedAt),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                PriorityLabel(priority = workOrder.priority)
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (containerColor, contentColor) = when (status.uppercase()) {
        "COMPLETED" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32) // Green
        "IN_PROGRESS" -> Color(0xFFFFF3E0) to Color(0xFFEF6C00) // Orange
        "ASSIGNED" -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = DisplayFormatter.toLabel(status),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PriorityLabel(priority: String) {
    val color = when (priority.uppercase()) {
        "URGENT", "CRITICAL" -> MaterialTheme.colorScheme.error
        "HIGH" -> Color(0xFFD32F2F)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = DisplayFormatter.toLabel(priority),
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = if (priority.uppercase() == "URGENT") androidx.compose.ui.text.font.FontWeight.Bold else null
        )
    }
}
