//package td.info507.mymarket.ui
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import td.info507.mymarket.modele.Article
//import td.info507.mymarket.viewmodel.CourseViewModel
//
//@Composable
//fun CourseScreen(viewModel: CourseViewModel) {
//    val course = viewModel.course.value ?: run {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Aucune course") }
//        return
//    }
//    val articles = viewModel.articles
//
//    var showAddDialog by remember { mutableStateOf(false) }
//    var editIndex by remember { mutableStateOf<Int?>(null) }
//    var showConfirmFinish by remember { mutableStateOf(false) }
//
//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(text = course.nom, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
//        Text("${course.date} • ${course.lieu}", color = Color.Gray)
//
//        Spacer(Modifier.height(12.dp))
//
//        LazyColumn(Modifier.weight(1f)) {
//            itemsIndexed(articles) { index, a ->
//                ArticleRow(
//                    article = a,
//                    enabled = !course.etat,
//                    onToggle = { viewModel.toggle(index) },
//                    onPriceClick = { editIndex = index }
//                )
//                Spacer(Modifier.height(8.dp))
//            }
//        }
//
//        Spacer(Modifier.height(8.dp))
//
//        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//            if (!course.etat) {
//                FilledTonalButton(
//                    onClick = { showAddDialog = true },
//                    shape = MaterialTheme.shapes.large,
//                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//                ) { Text("+") }
//            }
//        }
//
//        Spacer(Modifier.height(12.dp))
//
//        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            AssistChip(label = { Text("Budget final : ${viewModel.budgetFinal()}$") }, onClick = {})
//        }
//
//        Spacer(Modifier.height(12.dp))
//
//        if (!course.etat) {
//            Button(
//                onClick = { showConfirmFinish = true },
//                modifier = Modifier.fillMaxWidth()
//            ) { Text("Terminer la course") }
//        } else {
//            TextButton(
//                onClick = { viewModel.reopen() },
//                modifier = Modifier.fillMaxWidth()
//            ) { Text("Reprendre la course") }
//        }
//    }
//
//    if (showAddDialog) {
//        AddArticleDialog(
//            onDismiss = { showAddDialog = false },
//            onConfirm = { name, priceEstime ->
//                viewModel.addArticle(name, priceEstime)
//                showAddDialog = false
//            }
//        )
//    }
//
//    editIndex?.let { idx ->
//        EditPriceDialog(
//            initial = articles[idx].price_final,
//            onDismiss = { editIndex = null },
//            onConfirm = { newPrice ->
//                viewModel.setFinalPrice(idx, newPrice)
//                editIndex = null
//            }
//        )
//    }
//
//    if (showConfirmFinish) {
//        AlertDialog(
//            onDismissRequest = { showConfirmFinish = false },
//            confirmButton = {
//                TextButton(onClick = {
//                    viewModel.finish()
//                    showConfirmFinish = false
//                }) { Text("Oui, terminer") }
//            },
//            dismissButton = { TextButton(onClick = { showConfirmFinish = false }) { Text("Annuler") } },
//            title = { Text("Terminer la course") },
//            text = { Text("Êtes-vous sûr de vouloir terminer la course ?") }
//        )
//    }
//}
//
//@Composable
//private fun ArticleRow(
//    article: Article,
//    enabled: Boolean,
//    onToggle: () -> Unit,
//    onPriceClick: () -> Unit
//) {
//    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
//        Checkbox(
//            checked = article.checked,
//            onCheckedChange = { if (enabled) onToggle() },
//            enabled = enabled,
//            colors = CheckboxDefaults.colors()
//        )
//
//        Text(article.name, Modifier.weight(1f))
//
//        TextButton(onClick = { if (enabled) onPriceClick() }, enabled = enabled) {
//            Text("${article.price_final}$")
//        }
//    }
//}
//
//@Composable
//private fun AddArticleDialog(
//    onDismiss: () -> Unit,
//    onConfirm: (name: String, priceEstime: Int) -> Unit
//) {
//    var name by remember { mutableStateOf("") }
//    var price by remember { mutableStateOf("") }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        confirmButton = {
//            TextButton(onClick = {
//                if (name.isNotBlank() && price.isNotBlank()) {
//                    onConfirm(name.trim(), price.toIntOrNull() ?: 0)
//                }
//            }) { Text("Ajouter") }
//        },
//        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } },
//        title = { Text("Nouvel article") },
//        text = {
//            Column {
//                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nom") })
//                Spacer(Modifier.height(8.dp))
//                OutlinedTextField(
//                    value = price,
//                    onValueChange = { s -> price = s.filter { it.isDigit() } },
//                    label = { Text("Prix estimé (entier)") }
//                )
//            }
//        }
//    )
//}
//
//@Composable
//private fun EditPriceDialog(
//    initial: Int,
//    onDismiss: () -> Unit,
//    onConfirm: (newPrice: Int) -> Unit
//) {
//    var price by remember { mutableStateOf(initial.toString()) }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        confirmButton = {
//            TextButton(onClick = { onConfirm(price.toIntOrNull() ?: 0) }) { Text("Enregistrer") }
//        },
//        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } },
//        title = { Text("Modifier le prix") },
//        text = {
//            OutlinedTextField(
//                value = price,
//                onValueChange = { s -> price = s.filter { it.isDigit() } },
//                label = { Text("Prix final (entier)") }
//            )
//        }
//    )
//}
