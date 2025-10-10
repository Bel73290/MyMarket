package tp.info507.mymarket

import android.R.attr.start
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import td.info507.mymarket.ui.theme.MyMarketTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMarketTheme {
                ListeEvenement()
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun ListeEvenement() {
    Column() {
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
                    .padding(end = 15.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),

                    onClick = {
                        isVisible_CourseT = !isVisible_CourseT
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),
                        contentColor = Color.Black    )
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
                onClick = { isVisible = !isVisible } // change l'état à chaque clic
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
                    .padding(end = 15.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),


                    onClick = { isVisible = !isVisible },
                    colors = ButtonDefaults.buttonColors(Color(0xFFD9D9D9),
                        contentColor = Color.Black    )


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

        val showDialog2 = remember { mutableStateOf(false)}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.Bottom,

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_map),
                        contentDescription = "Map",
                        tint = Color.Black,
                        modifier = Modifier.size(40.dp)
                    )
                }
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
                IconButton(onClick = { }) {
                    Icon(

                        painter = painterResource(R.drawable.ic_cloche),
                        contentDescription = "Galerie",
                        tint = Color.Black,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Dialogue(showDialog2)
        }

    }

}


@Composable
fun Dialogue(showDialog: MutableState<Boolean>){
    var Nom by remember { mutableStateOf("") }
    var Magasin by remember { mutableStateOf("") }
    var Budget by remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Ajouter une liste") },
            text = {
                Column {
                    var NomText by remember { mutableStateOf(Nom) }
                    var MagasinText by remember { mutableStateOf(Magasin) }
                    var BudgetText by remember { mutableStateOf(Budget) }
                    OutlinedTextField(
                        value = NomText,
                        onValueChange = { NomText = it },
                        label = { Text("Nom") }
                    )
                    OutlinedTextField(
                        value = MagasinText,
                        onValueChange = { MagasinText = it },
                        label = { Text("Magasin") }
                    )
                    OutlinedTextField(
                        value = BudgetText,
                        onValueChange = { BudgetText = it },
                        label = { Text("Bugdget") }
                    )
                }
            },


            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Annuler")
                }
            },
            confirmButton = {
                Button(
                    modifier = Modifier,
                    onClick = { showDialog.value = false }
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