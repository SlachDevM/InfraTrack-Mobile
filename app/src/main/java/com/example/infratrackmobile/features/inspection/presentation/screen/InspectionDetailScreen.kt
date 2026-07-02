package com.example.infratrackmobile.features.inspection.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar("Answers saved successfully")
            viewModel.clearSaveSuccess()
        }
    }

    LaunchedEffect(uiState.completeSuccess) {
        if (uiState.completeSuccess) {
            onBackClick()
        }
    }

    if (uiState.showCompletionDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideCompletionDialog,
            title = { Text("Complete Inspection") },
            text = { Text("Are you sure you want to complete this inspection? This action is final for this step.") },
            confirmButton = {
                TextButton(onClick = viewModel::completeInspection) {
                    Text("Complete")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideCompletionDialog) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
            if (uiState.isLoading && uiState.bundle == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null && uiState.bundle == null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = viewModel::loadBundle) {
                        Text("Retry")
                    }
                }
            } else {
                uiState.bundle?.let { bundle ->
                    InspectionBundleContent(
                        bundle = bundle,
                        editableAnswers = uiState.editableAnswers,
                        observedCondition = uiState.observedCondition,
                        observations = uiState.observations,
                        issueIdentified = uiState.issueIdentified,
                        isSaving = uiState.isSaving,
                        isCompleting = uiState.isCompleting,
                        onBooleanChange = viewModel::updateBooleanAnswer,
                        onTextChange = viewModel::updateTextAnswer,
                        onNumberChange = viewModel::updateNumberAnswer,
                        onChoiceChange = viewModel::updateChoiceAnswer,
                        onConditionChange = viewModel::updateObservedCondition,
                        onObservationsChange = viewModel::updateObservations,
                        onIssueIdentifiedChange = viewModel::updateIssueIdentified,
                        onSaveClick = viewModel::saveAnswers,
                        onCompleteClick = viewModel::showCompletionDialog
                    )
                }
            }
        }
    }
}

@Composable
private fun InspectionBundleContent(
    bundle: InspectionBundle,
    editableAnswers: Map<Long, InspectionAnswerInput>,
    observedCondition: PhysicalCondition?,
    observations: String,
    issueIdentified: Boolean,
    isSaving: Boolean,
    isCompleting: Boolean,
    onBooleanChange: (Long, Boolean) -> Unit,
    onTextChange: (Long, String) -> Unit,
    onNumberChange: (Long, String) -> Unit,
    onChoiceChange: (Long, String) -> Unit,
    onConditionChange: (PhysicalCondition) -> Unit,
    onObservationsChange: (String) -> Unit,
    onIssueIdentifiedChange: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    val isReadOnly = bundle.inspection.status == "COMPLETED"

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
            InspectionSummarySection(
                inspection = bundle.inspection,
                observedCondition = observedCondition,
                observations = observations,
                issueIdentified = issueIdentified,
                onConditionChange = onConditionChange,
                onObservationsChange = onObservationsChange,
                onIssueIdentifiedChange = onIssueIdentifiedChange,
                isReadOnly = isReadOnly
            )
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
                val answer = editableAnswers[question.id]
                QuestionItem(
                    question = question,
                    answer = answer,
                    onBooleanChange = { onBooleanChange(question.id, it) },
                    onTextChange = { onTextChange(question.id, it) },
                    onNumberChange = { onNumberChange(question.id, it) },
                    onChoiceChange = { onChoiceChange(question.id, it) },
                    isReadOnly = isReadOnly
                )
            }
        }

        if (!isReadOnly) {
            item {
                SectionHeader("Actions")
                AllowedActionsSection(
                    allowedActions = bundle.allowedActions,
                    isSaving = isSaving,
                    isCompleting = isCompleting,
                    onSaveClick = onSaveClick,
                    onCompleteClick = onCompleteClick
                )
            }
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
private fun InspectionSummarySection(
    inspection: InspectionSummary,
    observedCondition: PhysicalCondition?,
    observations: String,
    issueIdentified: Boolean,
    onConditionChange: (PhysicalCondition) -> Unit,
    onObservationsChange: (String) -> Unit,
    onIssueIdentifiedChange: (Boolean) -> Unit,
    isReadOnly: Boolean = false
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Status: ${inspection.status}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Priority: ${inspection.priority}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Expected: ${inspection.expectedCompletionDate}", style = MaterialTheme.typography.bodyMedium)
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            
            Text(text = "Completion Data", style = MaterialTheme.typography.titleSmall)

            Text(text = "Observed Condition *", style = MaterialTheme.typography.labelSmall)
            ConditionSelector(
                selectedCondition = observedCondition,
                onConditionSelected = onConditionChange,
                enabled = !isReadOnly
            )

            OutlinedTextField(
                value = observations,
                onValueChange = onObservationsChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Observations *") },
                minLines = 3,
                readOnly = isReadOnly,
                enabled = !isReadOnly
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = issueIdentified,
                    onCheckedChange = onIssueIdentifiedChange,
                    enabled = !isReadOnly
                )
                Text(text = "Issue Identified", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ConditionSelector(
    selectedCondition: PhysicalCondition?,
    onConditionSelected: (PhysicalCondition) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PhysicalCondition.entries.forEach { condition ->
            FilterChip(
                selected = condition == selectedCondition,
                onClick = { onConditionSelected(condition) },
                label = { 
                    Text(
                        text = condition.name,
                        style = MaterialTheme.typography.labelSmall
                    ) 
                },
                modifier = Modifier.weight(1f),
                enabled = enabled
            )
        }
    }
}

@Composable
private fun QuestionItem(
    question: InspectionQuestion,
    answer: InspectionAnswerInput?,
    onBooleanChange: (Boolean) -> Unit,
    onTextChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onChoiceChange: (String) -> Unit,
    isReadOnly: Boolean = false
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(text = question.questionText, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                if (question.required && !isReadOnly) {
                    Text(text = "*", color = MaterialTheme.colorScheme.error)
                }
            }
            if (question.helpText != null) {
                Text(text = question.helpText, style = MaterialTheme.typography.bodySmall)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when (question.type) {
                "BOOLEAN" -> {
                    BooleanAnswerSelector(
                        value = answer?.booleanValue,
                        onValueChange = onBooleanChange,
                        enabled = !isReadOnly
                    )
                }
                "TEXT" -> {
                    OutlinedTextField(
                        value = answer?.textValue ?: "",
                        onValueChange = onTextChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Your answer") },
                        readOnly = isReadOnly,
                        enabled = !isReadOnly
                    )
                }
                "NUMBER" -> {
                    OutlinedTextField(
                        value = answer?.numberValue?.toString() ?: "",
                        onValueChange = onNumberChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Value ${question.unitSymbol ?: ""}") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        readOnly = isReadOnly,
                        enabled = !isReadOnly
                    )
                }
                "CHOICE" -> {
                    ChoiceAnswerSelector(
                        selectedCode = answer?.choiceCodeValue,
                        choices = question.choices,
                        onChoiceSelected = onChoiceChange,
                        enabled = !isReadOnly
                    )
                }
            }
        }
    }
}

@Composable
private fun BooleanAnswerSelector(
    value: Boolean?,
    onValueChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = value == true,
            onClick = { onValueChange(true) },
            label = { Text("Yes") },
            enabled = enabled
        )
        FilterChip(
            selected = value == false,
            onClick = { onValueChange(false) },
            label = { Text("No") },
            enabled = enabled
        )
    }
}

@Composable
private fun ChoiceAnswerSelector(
    selectedCode: String?,
    choices: List<InspectionChoice>,
    onChoiceSelected: (String) -> Unit,
    enabled: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        choices.forEach { choice ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = choice.code == selectedCode,
                    onClick = { onChoiceSelected(choice.code) },
                    enabled = enabled
                )
                Text(text = choice.label, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun AllowedActionsSection(
    allowedActions: InspectionAllowedActions,
    isSaving: Boolean,
    isCompleting: Boolean,
    onSaveClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving && !isCompleting
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Save Answers")
            }
        }

        if (allowedActions.canComplete) {
            Button(
                onClick = onCompleteClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                enabled = !isSaving && !isCompleting
            ) {
                if (isCompleting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Complete Inspection")
                }
            }
        }

        if (allowedActions.canUploadDocument) {
            OutlinedButton(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), enabled = false) {
                Text("Upload Document (Planned)")
            }
        }
    }
}
