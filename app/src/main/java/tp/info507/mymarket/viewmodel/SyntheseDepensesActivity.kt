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

    // Extrait le mois et l’année du format jj/mm/aaaa
    fun extractMonthYear(date: String): Pair<String, String> {
        val parts = date.trim().split("/")
        val mois = parts.getOrNull(1).orEmpty()
        val annee = parts.getOrNull(2).orEmpty()
        return mois to annee
    }

    // Associe les numéros de mois à leur nom
    val moisLabel = mapOf(
        "01" to "Janvier", "02" to "Février", "03" to "Mars", "04" to "Avril",
        "05" to "Mai", "06" to "Juin", "07" to "Juillet", "08" to "Août",
        "09" to "Septembre", "10" to "Octobre", "11" to "Novembre", "12" to "Décembre"
    )

    LaunchedEffect(Unit) {
        val courses = courseCrud.getAll()
        val map = linkedMapOf<String, Int>() // clé : "aaaa/mm"

        for (c in courses) {
            if (!c.etat) continue //On ne garde que les courses terminées
            val (mois, annee) = extractMonthYear(c.date)
            if (mois.length != 2 || annee.isEmpty()) continue
            val key = "$annee/$mois"
            val finalCourse = caCrud.budgetFinalAfter(c.id)
            map[key] = (map[key] ?: 0) + finalCourse
        }

        //Trie chronologiquement et transforme les clés en labels lisibles
        parMois = map.entries
            .sortedBy { it.key } // "2025/01", "2025/02", ...
            .map { (k, v) ->
                val parts = k.split("/")
                val label = "${moisLabel[parts[1]] ?: "Mois ${parts[1]}"} ${parts[0]}"
                label to v
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(45.dp),
        horizontalAlignment = Alignment.Start
    ) {
        //Bouton retour
        IconButton(
            modifier = Modifier.padding(start = 0.dp),
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_fleche_gauche),
                contentDescription = "Retour",
                tint = Color.Gray
            )
        }

        Text(
            text = "Dépenses mensuelles",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        //Liste des dépenses
        for ((label, montant) in parMois) {
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
                    Text(label, modifier = Modifier.weight(1f))
                    Text("$montant €", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        if (parMois.isEmpty()) {
            Text("Aucune dépense trouvée (courses terminées).", color = Color.Gray)
        }
    }
}
