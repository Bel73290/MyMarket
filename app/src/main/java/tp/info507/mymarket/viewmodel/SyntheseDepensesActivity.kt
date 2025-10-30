package tp.info507.mymarket.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import td.info507.mymarket.crud.CourseArticleCrud
import td.info507.mymarket.crud.CourseCrud
import td.info507.mymarket.ui.theme.MyMarketTheme
import tp.info507.mymarket.MainActivity
import tp.info507.mymarket.R

class SyntheseDepensesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMarketTheme {
                SyntheseDepensesScreen()
            }
        }
    }
}

@Composable
fun SyntheseDepensesScreen() {
    val context = LocalContext.current
    val courseCrud = remember { CourseCrud(context) }
    val caCrud = remember { CourseArticleCrud(context) }

    var parMois by remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }

    // "jj/mm/aaaa" ou "aaaa-mm-jj" -> "01".."12"
    fun extractMonth(date: String): String {
        val d = date.trim()
        return when {
            d.contains("/") && d.split("/").size >= 2 -> d.split("/")[1]
            else -> "??"
        }
    }
    val moisLabel = mapOf(
        "01" to "Janvier", "02" to "Février", "03" to "Mars", "04" to "Avril",
        "05" to "Mai", "06" to "Juin", "07" to "Juillet", "08" to "Août",
        "09" to "Septembre", "10" to "Octobre", "11" to "Novembre", "12" to "Décembre"
    )

    LaunchedEffect(Unit) {
        val courses = courseCrud.getAll()
        val map = linkedMapOf<String, Int>()
        for (c in courses) {
            val mois = extractMonth(c.date)
            val finalCourse = caCrud.budgetFinalAfter(c.id)
            map[mois] = (map[mois] ?: 0) + finalCourse
        }
        parMois = map.entries
            .sortedBy { it.key.padStart(2, '0') }
            .map { (m, v) -> (moisLabel[m] ?: "Mois $m") to v }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(45.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Retour simple à l'écran précédent
        IconButton(
            modifier = Modifier.padding(start = 0.dp),
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_fleche_gauche),
                contentDescription = "Galerie",
                tint = Color.Gray
            )
        }

        Text(
            text = "Dépenses mensuelles",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Liste des mois
        for ((mois, montant) in parMois) {
            Surface(
                tonalElevation = 0.dp,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(mois, modifier = Modifier.weight(1f))
                    Text("$montant €", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        if (parMois.isEmpty()) {
            Text("Aucune dépense trouvée.", color = Color.Gray)
        }
    }
}
