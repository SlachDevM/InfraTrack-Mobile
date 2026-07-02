package com.example.infratrackmobile.core.ui.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DisplayFormatter {

    private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    fun toLabel(value: String?): String {
        if (value == null || value.isBlank()) return "Not available"
        
        return when (value.uppercase()) {
            "FIELD_EMPLOYEE" -> "Field Employee"
            "MANAGER" -> "Manager"
            "ASSIGNED" -> "Assigned"
            "IN_PROGRESS" -> "In Progress"
            "COMPLETED" -> "Completed"
            "URGENT" -> "Urgent"
            "HIGH" -> "High Priority"
            "MEDIUM" -> "Medium Priority"
            "LOW" -> "Low Priority"
            "GOOD" -> "Good"
            "FAIR" -> "Fair"
            "POOR" -> "Poor"
            "CRITICAL" -> "Critical"
            "INTERNAL_MAINTENANCE" -> "Internal Maintenance"
            "CONTRACTOR_WORK" -> "Contractor Work"
            else -> value.replace("_", " ")
                .lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }

    fun formatDate(isoDate: String?): String {
        if (isoDate == null || isoDate.isBlank()) return "Not available"
        return try {
            val date = if (isoDate.contains("T")) {
                LocalDateTime.parse(isoDate).toLocalDate()
            } else {
                LocalDate.parse(isoDate)
            }
            formatRelativeDate(date)
        } catch (e: Exception) {
            isoDate
        }
    }

    fun formatDateTime(isoDateTime: String?): String {
        if (isoDateTime == null || isoDateTime.isBlank()) return "Not available"
        return try {
            val dateTime = LocalDateTime.parse(isoDateTime)
            val relativeDate = formatRelativeDate(dateTime.toLocalDate())
            val time = dateTime.format(timeFormatter)
            "$relativeDate • $time"
        } catch (e: Exception) {
            isoDateTime
        }
    }

    private fun formatRelativeDate(date: LocalDate): String {
        val today = LocalDate.now()
        return when (date) {
            today -> "Today"
            today.minusDays(1) -> "Yesterday"
            today.plusDays(1) -> "Tomorrow"
            else -> date.format(dateFormatter)
        }
    }

    fun yesNo(value: Boolean?): String {
        return when (value) {
            true -> "Yes"
            false -> "No"
            null -> "Not available"
        }
    }

    fun issueStatus(issueIdentified: Boolean?): String {
        return when (issueIdentified) {
            true -> "Issue detected"
            false -> "No issue detected"
            null -> "Not available"
        }
    }

    fun orNotAvailable(value: String?): String {
        return if (value.isNullOrBlank()) "Not available" else value
    }
}
