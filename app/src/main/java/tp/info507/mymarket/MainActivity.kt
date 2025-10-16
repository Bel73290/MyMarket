package tp.info507.mymarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import td.info507.mymarket.crud.CourseCrud
import td.info507.mymarket.ui.theme.MyMarketTheme

import tp.info507.mymarket.viewmodel.CourseLScreen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMarketTheme {//test
                ListeEvenement()
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun ListeEvenement() {
    val context = LocalContext.current
    Column(modifier= Modifier
        .verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier
                .padding(top = 45.dp)
                .padding(start = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Image(
                painter = painterResource(R.drawable.ic_profil),
                contentDescription = "Profile Image",
            )


            val text = remember {
                mutableStateOf("")
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(0.90f)
                    .height(65.dp)
                    .padding(end = 15.dp)
                    .background(colorResource(id = R.color.search)),


                value = text.value,
                onValueChange = { newText -> text.value = newText },

                label = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }

            )
        }
        var isVisible_CourseT by remember { mutableStateOf(true) }
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Course à faire",
                modifier = Modifier.padding(6.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp
            )
            IconButton(
                onClick = { isVisible_CourseT = !isVisible_CourseT },

                ) {
                Icon(
                    painter = painterResource(R.drawable.ic_fleche),
                    contentDescription = "Galerie",
                    tint = Color.Gray
                )
            }
        }
        if (isVisible_CourseT) {
            Column(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .padding(end = 15.dp),

                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val courses = CourseCrud(context).getAll()
                for (course in courses) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            val intent = Intent(context, CourseLScreen::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFFD9D9D9),
                            contentColor = Color.Black
                        )


                    ) {

                        Column() {
                            Text("Nom: ${course.nom}")
                            Text("Nombre d'articles: ${course.id}")
                        }
                        Column(modifier = Modifier.padding(start = 56.dp)) {
                            Text("Budgets Final: ${course.prix_initial}$")

                        }

                    }


                }
            }
        }
        var isVisible by remember { mutableStateOf(true) }
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Course Terminer",
                modifier = Modifier.padding(6.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp
            )



            IconButton(
                onClick = { isVisible = !isVisible }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_fleche),
                    contentDescription = "Galerie",
                    tint = Color.Gray
                )
            }

        }

        if (isVisible) {

            Column(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .padding(end = 15.dp),

                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(), onClick = {
                        val intent = Intent(context, CourseLScreen::class.java)
                        context.startActivity(intent)
                    }, colors = ButtonDefaults.buttonColors(
                        Color(0xFFD9D9D9),
                        contentColor = Color.Black
                    )
                ) {
                    Column() {
                        Text("Nom: Course 1")
                        Text("Nombre d'articles: 50")
                    }
                    Column(modifier = Modifier.padding(start = 56.dp)) {
                        Text("Budgets Final: 100$")

                    }
                }

            }
        }
    }
        val showDialog2 = remember { mutableStateOf(false)}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                ,
            verticalArrangement = Arrangement.Bottom,

            ) {
            Row(
                modifier = Modifier.fillMaxWidth()

                    .background(Color.White),
                horizontalArrangement = Arrangement.Center,


            ) {

                IconButton(onClick = {
                    showDialog2.value = true
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_caddis),
                        contentDescription = "caddie",
                        tint = Color.Black,
                        modifier = Modifier.size(45.dp)
                    )
                }

            }
            Dialogue(showDialog2)
        }

    }





@Composable
fun Dialogue(showDialog: MutableState<Boolean>){
    val context = LocalContext.current
    var NomText by remember { mutableStateOf("") }
    var DateText by remember { mutableStateOf("") }
    var BudgetText by remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Ajouter une liste") },
            text = {
                Column {

                    OutlinedTextField(
                        value = NomText,
                        onValueChange = { NomText = it },
                        label = { Text("Nom") }
                    )
                    OutlinedTextField(
                        value = DateText,
                        onValueChange = { DateText = it },
                        label = { Text("Date") }
                    )
                    OutlinedTextField(
                        value = BudgetText,
                        onValueChange = { BudgetText = it },
                        label = { Text("Bugdget") }
                    )


                }
            },


            dismissButton = {
                Button(modifier=Modifier
                    .fillMaxWidth(),
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
                    modifier=Modifier
                        .fillMaxWidth(),
                    onClick = {
                        val budgetInt: Int = BudgetText.toIntOrNull() ?: 0 // valeur par défaut si vide
                        val storage = CourseCrud(context)
                        storage.createCourse(NomText, DateText.ifEmpty { "2025-10-10" }, budgetInt)

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
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMarketTheme {
        ListeEvenement()
    }
}