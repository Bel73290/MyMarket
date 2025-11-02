package tp.info507.mymarket.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import td.info507.mymarket.crud.CourseArticleCrud
import td.info507.mymarket.modele.Article
import td.info507.mymarket.ui.theme.MyMarketTheme
import tp.info507.mymarket.ListeEvenement
import tp.info507.mymarket.MainActivity
import tp.info507.mymarket.R
import tp.info507.mymarket.viewmodel.Dialogue_ajout_article
import tp.info507.mymarket.viewmodel.Test2

//import td.info507.mymarket.viewmodel.CourseViewModel



class CourseScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val courseId = intent.getIntExtra("COURSE_ID", -1)
        val prix_initial = intent.getIntExtra("prix_initial", -1)
        setContent {
            MyMarketTheme {
                Test(courseId, prix_initial)
            }
        }
    }
}


@Composable
fun Test(courseId: Int, prix_initial: Int) {
    val context = LocalContext.current
    val crud = CourseArticleCrud(context)
    var articles by remember { mutableStateOf(crud.getItems(courseId)) }
    var total by remember { mutableStateOf(crud.budgetFinalAfter(courseId)) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier

                .background(Color.White)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(top = 45.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_fleche_gauche),
                        contentDescription = "Retour",
                        tint = Color.Gray
                    )
                }
                Text(
                    text = "Course $courseId",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Liste des articles
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp, bottom = 200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                articles.forEach { (courseArticle, name) ->
                    var valeur by remember { mutableStateOf(courseArticle.price_final.toString()) }

                    Row(
                        modifier = Modifier.padding(end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Case cochée
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    color = if (courseArticle.checked) Color.Green else Color.Red,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (courseArticle.checked) {
                                Text("✓", color = Color.White, textAlign = TextAlign.Center)
                            } else {
                                Text("X", color = Color.White, textAlign = TextAlign.Center)
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = name,
                            textDecoration = if (courseArticle.checked) TextDecoration.LineThrough else TextDecoration.None
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Zone du prix
                        Box(
                            modifier = Modifier
                                .size(width = 80.dp, height = 40.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(25.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(0.9f)
                                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = valeur + "€",
                                    color = Color.Black,
                                    fontSize = 16.sp // tu peux ajuster la taille comme tu veux
                                )

                            }
                        }
                    }
                }
            }
        }
    }
        Column(
            modifier = Modifier

                .fillMaxHeight()

                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
        ) {

            Column(
                modifier = Modifier

                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Budget estimé (prix_initial)
                    Box(
                        modifier = Modifier
                            .size(width = 210.dp, height = 40.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(25.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Budget estimé", color = Color.White)
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = prix_initial.toString(), color = Color.Black)
                            }
                            Text("€", color = Color.White)
                        }
                    }

                    // Total réel (somme des PRICE_FINAL)
                    Box(
                        modifier = Modifier
                            .size(width = 210.dp, height = 40.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(25.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total réel", color = Color.White)
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = total.toString(), color = Color.Black)
                            }
                            Text("€", color = Color.White)
                        }
                    }
                }


                // Bouton "Terminer Course"
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Quitter Course", color = Color.White)
                }
            }
        }

    }

