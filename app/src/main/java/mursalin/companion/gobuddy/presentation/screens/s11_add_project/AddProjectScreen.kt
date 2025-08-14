package mursalin.companion.gobuddy.presentation.screens.s11_add_project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import mursalin.companion.gobuddy.presentation.viewmodel.AddProjectViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(
    navController: NavController,
    viewModel: AddProjectViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isProjectAdded) {
        if (state.isProjectAdded) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.generationError) {
        state.generationError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add New Project") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // AI Generation Section
            Text("Generate with AI", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = state.aiIdea,
                onValueChange = { viewModel.onAiIdeaChanged(it) },
                label = { Text("Describe your project idea...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            Button(
                onClick = { viewModel.generateProjectPlan() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isGenerating
            ) {
                if (state.isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Generate Plan")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Project Details Section
            Text("Project Details", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onTitleChanged(it) },
                label = { Text("Project Title") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.titleError != null
            )
            if (state.titleError != null) {
                Text(state.titleError!!, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChanged(it) },
                label = { Text("Project Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            // Generated Tasks Section
            state.generatedPlan?.let { plan ->
                Text("Generated Tasks", style = MaterialTheme.typography.titleMedium)
                plan.tasks.forEach { task ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(task.title, modifier = Modifier.weight(1f))
                            Text(task.duration, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }


            DateSelector(
                label = "Start Date",
                date = state.startDate,
                onDateSelected = { viewModel.onStartDateChanged(it) }
            )

            DateSelector(
                label = "End Date",
                date = state.endDate,
                onDateSelected = { viewModel.onEndDateChanged(it) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.addProject() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Create Project")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelector(label: String, date: Date, onDateSelected: (Date) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val calendar = Calendar.getInstance().apply { time = date }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val newCalendar = Calendar.getInstance()
            newCalendar.set(selectedYear, selectedMonth, selectedDay)
            onDateSelected(newCalendar.time)
        }, year, month, day
    )

    OutlinedTextField(
        value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date),
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }
    )
}
