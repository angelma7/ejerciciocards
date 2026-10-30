package com.example.myapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// 1. Definimos las prioridades posibles para las tareas
enum class Priority {
    ALTA, MEDIA, BAJA
}

// 2. Modelo de datos para cada Tarea
data class Task(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val priority: Priority
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    TaskScreen()
                }
            }
        }
    }
}

@Composable
fun TaskScreen() {
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(1, "Hacer la comida", false, Priority.ALTA),
                Task(2, "Ir al  gym", false, Priority.MEDIA),
                Task(3, "Sacar al perro", true, Priority.MEDIA)
            )
        )
    }

    val pendingTasks = tasks.filter { !it.isCompleted }
    val completedTasks = tasks.filter { it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Tareas",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // TAREAS PENDIENTES
            items(pendingTasks) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = {
                        tasks = tasks.map { if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it }
                    },
                    onPriorityChange = { newPriority ->
                        tasks = tasks.map { if (it.id == task.id) it.copy(priority = newPriority) else it }
                    }
                )
            }

            // Divider para separar pendientes de completadas
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Completadas",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // TAREAS COMPLETADAS
            items(completedTasks) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = {
                        tasks = tasks.map { if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it }
                    },
                    onPriorityChange = { newPriority ->
                        tasks = tasks.map { if (it.id == task.id) it.copy(priority = newPriority) else it }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: () -> Unit,
    onPriorityChange: (Priority) -> Unit
) {
    var expandedMenu1 by remember { mutableStateOf(false) }
    var expandedMenu2 by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onCheckedChange() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.body1
            )

            Box {
                BadgedBox(
                    badge = {
                        Badge {
                            Text(text = task.priority.name)
                        }
                    },
                    modifier = Modifier.clickable { expandedMenu2 = true }
                ) {
                    Text(
                        text = "       ",
                        modifier = Modifier.padding(4.dp)
                    )
                }

                DropdownMenu(
                    expanded = expandedMenu2,
                    onDismissRequest = { expandedMenu2 = false }
                ) {
                    DropdownMenuItem(onClick = {
                        onPriorityChange(Priority.ALTA)
                        expandedMenu2 = false
                    }) {
                        Text("Alta")
                    }
                    DropdownMenuItem(onClick = {
                        onPriorityChange(Priority.MEDIA)
                        expandedMenu2 = false
                    }) {
                        Text("Media")
                    }
                    DropdownMenuItem(onClick = {
                        onPriorityChange(Priority.BAJA)
                        expandedMenu2 = false
                    }) {
                        Text("Baja")
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box {
                IconButton(onClick = { expandedMenu1 = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }

                DropdownMenu(
                    expanded = expandedMenu1,
                    onDismissRequest = { expandedMenu1 = false }
                ) {
                    DropdownMenuItem(onClick = {
                        onCheckedChange()
                        expandedMenu1 = false
                    }) {
                        Text(if (task.isCompleted) "Marcar como pendiente" else "Marcar como completada")
                    }
                }
            }
        }
    }
}