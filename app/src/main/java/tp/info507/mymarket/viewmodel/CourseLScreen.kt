package tp.info507.mymarket.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import td.info507.mymarket.crud.ArticleCrud
import td.info507.mymarket.crud.CourseArticleCrud
import td.info507.mymarket.crud.CourseCrud
import td.info507.mymarket.ui.theme.MyMarketTheme
import tp.info507.mymarket.Dialogue
import tp.info507.mymarket.ListeEvenement
import tp.info507.mymarket.MainActivity
import tp.info507.mymarket.R
import kotlin.text.ifEmpty

class CourseLScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val courseId = intent.getIntExtra("COURSE_ID", -1)
        val prix_initial = intent.getIntExtra("prix_initial", -1)
        prix_initial
        setContent {
            MyMarketTheme {
                Test2(courseId,prix_initial)
            }
        }
    }
}
@Composable
fun Test2(courseId: Int, prix_initial: Int) {
    val context = LocalContext.current
    val crud = CourseArticleCrud(context)
    var valeur by remember { mutableStateOf("") }
    var courses by remember { mutableStateOf(crud.getItems(courseId)) }

    Column() {
        Box {
            Row(
                modifier = Modifier.padding(top = 45.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    text = "Course 1",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }


        courses.forEach { (_, name) ->
            Log.d("DebugTest2", "Article affiché: $name")
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)

        ) {
            courses.forEach { (courseArticle, name) ->
                Row(
                    modifier = Modifier.padding(start = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                color = if (courseArticle.checked) Color.Green else Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(6.dp))
                            .clickable {
                                crud.toggleChecked(courseId, courseArticle.articleId)
                                courses = crud.getItems(courseId)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (courseArticle.checked) {
                            Text(
                                text = "✓",
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }


                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = name,
                        modifier = Modifier.padding(top = 3.dp),
                        textDecoration = if (courseArticle.checked) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Spacer(modifier = Modifier.weight(1f))



                    Box(
                        modifier = Modifier
                            .size(width = 80.dp, height = 40.dp)
                            .padding(end = 16.dp)
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
                            androidx.compose.foundation.text.BasicTextField(
                                value = valeur,
                                onValueChange = { newValue ->
                                    // garder uniquement les chiffres
                                    if (newValue.all { it.isDigit() }) {
                                        valeur = newValue
                                    }
                                },
                                singleLine = true,
                                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                            ) { innerTextField ->
                                if (valeur.isEmpty()) {
                                    androidx.compose.material3.Text(
                                        text = "0",
                                        color = Color.Gray
                                    )
                                }
                                innerTextField() // texte saisi
                            }
                        }
                    }



                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
        ) {
            val showDialogArticle = remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { showDialogArticle.value = true },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_circle_24),
                        contentDescription = "add",
                        tint = Color(0xFFD9D9D9),
                        modifier = Modifier.size(40.dp)
                    )
                }

                Dialogue_ajoue_article(
                    showDialog = showDialogArticle,
                    courseId = courseId,
                    ArticleAdd = {
                        courses = crud.getItems(courseId)
                    }
                )
            }

            Box(
                modifier = Modifier
                    .size(width = 210.dp, height = 40.dp)
                    .padding(end = 16.dp)
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
                    Text("Budget Estimer", color = Color.White)

                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .fillMaxHeight()
                            .background(Color.White, shape = RoundedCornerShape(25.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = prix_initial.toString(),
                            color = Color.Black
                        )

                    }
                    Text(
                        text ="€",
                        color = Color.White
                    )
                }
            }


            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                onClick = {val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Terminer Course",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun Dialogue_ajoue_article(showDialog: MutableState<Boolean>, courseId: Int, ArticleAdd: () -> Unit) {
    val context = LocalContext.current
    var NomText by remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Ajouter un Article") },
            text = {
                Column {
                    OutlinedTextField(
                        value = NomText,
                        onValueChange = { NomText = it },
                        label = { Text("Nom") }
                    )
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Annuler")
                }
            },
            confirmButton = {
                Button(onClick = {
                    val articleCrud = ArticleCrud(context)
                    val courseArticleCrud = CourseArticleCrud(context)

                    if (NomText.isNotBlank()) {
                        courseArticleCrud.createItem(courseId, NomText, 0)
                        ArticleAdd() // recharge la liste
                    }



                    showDialog.value = false
                }) {
                    Text("Valider")
                }
            }
        )
    }
}



@Preview(showBackground = true)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMarketTheme {
        Test2(courseId = 1,150)
    }
}
