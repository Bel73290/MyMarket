package tp.info507.mymarket.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
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
        setContent {
            MyMarketTheme {
                Test2(courseId)
            }
        }
    }
}
@Composable
fun Test2(courseId: Int) {
    val context = LocalContext.current
    Column() {
        Box() {
            Row(
                modifier = Modifier
                    .padding(top = 45.dp),

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


        val courses = CourseArticleCrud(context).getItems(courseId)
        Log.d("TestCourses", "Nombre d'articles récupérés : ${courses.size}")
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
                            .background(Color.Green, shape = RoundedCornerShape(6.dp))
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (courseArticle.checked) "✓" else "X",
                            color = if (courseArticle.checked) Color.White else Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(name, modifier = Modifier.padding(top = 3.dp))

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .size(80.dp, 40.dp)
                            .padding(end = 16.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(25.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(0.8f)
                                .background(Color.White, shape = RoundedCornerShape(25.dp))
                                .padding(4.dp)
                        ) {

                        }
                    }
                }
            }
        }



        Column (modifier = Modifier
            .fillMaxSize()
            .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),

        ){
            val showDialogArticle= remember { mutableStateOf(false)}
            Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
            IconButton(modifier = Modifier,
                onClick = {showDialogArticle.value = true},
            ) {
                Icon(

                    painter = painterResource(R.drawable.baseline_add_circle_24),
                    contentDescription = "add",
                    tint = Color(0xFFD9D9D9),
                    modifier = Modifier
                        .size(40.dp)
                )
            }

                Dialogue_ajoue_article(showDialogArticle, courseId = courseId)
        }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                onClick = {},
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
fun Dialogue_ajoue_article(showDialog: MutableState<Boolean>, courseId: Int){
    val context = LocalContext.current
    var NomText by remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Ajouter un Articles") },
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
                Button(modifier=Modifier,
                    onClick = { showDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFFFFFFFF),
                        contentColor = Color.Black
                    )) {
                    Text("Annuler")
                }
            },
            confirmButton = {
                Button(
                    modifier=Modifier,
                    onClick = {

                        val articleCrud = ArticleCrud(context)
                        val courseArticleCrud = CourseArticleCrud(context)

                        val articleId = articleCrud.createArticle(NomText).toInt()

                        courseArticleCrud.createItem(
                            courseId = courseId,
                            name = NomText,
                            priceEstime = 0
                        )

                        showDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF000000),
                        contentColor = Color.White
                    )
                ) {
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
        Test2(courseId = 1)
    }
}
