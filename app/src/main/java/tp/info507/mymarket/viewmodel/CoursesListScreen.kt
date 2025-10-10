package td.info507.mymarket.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import td.info507.mymarket.modele.Course
import td.info507.mymarket.viewmodel.CoursesListViewModel

@Composable
fun CoursesListScreen(
    viewModel: CoursesListViewModel,
    onCourseClick: (Course) -> Unit
) {
    LaunchedEffect(Unit) { viewModel.loadAll() }

    val (inProgress, finished) = viewModel.getGroupedCourses()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Courses en cours", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (inProgress.isEmpty()) Text("Aucune course en cours", color = Color.Gray)
        else LazyColumn {
            items(inProgress) { c ->
                CourseRow(c, Color(0xFF2ECC71)) { onCourseClick(c) }
            }
        }

        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(16.dp))

        Text("Courses terminées", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (finished.isEmpty()) Text("Aucune course terminée", color = Color.Gray)
        else LazyColumn {
            items(finished) { c ->
                CourseRow(c, Color(0xFFE74C3C)) { onCourseClick(c) }
            }
        }
    }
}

@Composable
private fun CourseRow(course: Course, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(course.nom, color = color, style = MaterialTheme.typography.titleMedium)
            Text("${course.date} • ${course.lieu}", color = Color.Gray)
        }
    }
}
