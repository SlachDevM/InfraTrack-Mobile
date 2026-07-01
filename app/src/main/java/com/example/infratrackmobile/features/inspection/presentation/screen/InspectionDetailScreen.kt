package com.example.infratrackmobile.features.inspection.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infratrackmobile.features.inspection.domain.model.*
import com.example.infratrackmobile.features.inspection.presentation.viewmodel.InspectionDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionDetailScreen(
    onBackClick: () -> Unit,
    viewModel: InspectionDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inspection Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = viewModel::loadBundle) {
                        Text("Retry")
                    }
                }
            } else {
                uiState.bundle?.let { bundle ->
                    InspectionBundleContent(bundle = bundle)
                }
            }
        }
    }
}

@Composable
private fun InspectionBundleContent(bundle: InspectionBundle) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader("Asset Information")
            AssetSummarySection(bundle.asset)
        }

        item {
            SectionHeader("Inspection Summary")
            InspectionSummarySection(bundle.inspection)
        }

        bundle.template?.let { template ->
            item {
                SectionHeader("Template: ${template.name}")
                Text("Version: ${template.version}", style = MaterialTheme.typography.bodySmall)
            }
        }

        if (bundle.questions.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text("No checklist for this inspection.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        } else {
            item {
                SectionHeader("Checklist")
            }
            items(bundle.questions.sortedBy { it.displayOrder }) { question ->
                val answer = bundle.answers.find { it.questionId == question.id }
                QuestionItem(question = question, answer = answer)
            }
        }

        item {
            SectionHeader("Actions")
            AllowedActionsSection(bundle.allowedActions)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun AssetSummarySection(asset: AssetSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = asset.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Category: ${asset.category}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Department: ${asset.department}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Location: ${asset.location}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun InspectionSummarySection(inspection: InspectionSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Status: ${inspection.status}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Priority: ${inspection.priority}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Expected: ${inspection.expectedCompletionDate}", style = MaterialTheme.typography.bodyMedium)
            inspection.observedCondition?.let {
                Text(text = "Condition: $it", style = MaterialTheme.typography.bodyMedium)
            }
            inspection.observations?.let {
                Text(text = "Observations: $it", style = MaterialTheme.typography.bodyMedium)
            }
            if (inspection.issueIdentified) {
                Text(text = "Issue Identified", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun QuestionItem(question: InspectionQuestion, answer: InspectionAnswer?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(text = question.questionText, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                if (question.required) {
                    Text(text = "*", color = MaterialTheme.colorScheme.error)
                }
            }
            if (question.helpText != null) {
                Text(text = question.helpText, style = MaterialTheme.typography.bodySmall)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = "Type: ${question.type}", style = MaterialTheme.typography.labelSmall)
            
            if (question.type == "CHOICE") {
                Text(text = "Choices:", style = MaterialTheme.typography.labelSmall)
                question.choices.sortedBy { it.displayOrder }.forEach { choice ->
                    Text(text = "- ${choice.label}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val answerText = when {
                answer == null -> "Not answered"
                answer.booleanValue != null -> answer.booleanValue.toString()
                answer.numberValue != null -> answer.numberValue.toString()
                answer.textValue != null -> answer.textValue
                answer.choiceCodeValue != null -> {
                    val choice = question.choices.find { it.code == answer.choiceCodeValue }
                    choice?.label ?: answer.choiceCodeValue
                }
                else -> "Not answered"
            }
            Text(text = "Current Answer: $answerText", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AllowedActionsSection(allowedActions: InspectionAllowedActions) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (allowedActions.canComplete) {
            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), enabled = false) {
                Text("Complete Inspection (Planned)")
            }
        }
        if (allowedActions.canUploadDocument) {
            OutlinedButton(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), enabled = false) {
                Text("Upload Document (Planned)")
            }
        }
    }
}
