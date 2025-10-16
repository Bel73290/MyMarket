package tp.info507.mymarket.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import td.info507.mymarket.ui.theme.MyMarketTheme
import tp.info507.mymarket.ListeEvenement
import tp.info507.mymarket.MainActivity
import tp.info507.mymarket.R

class CourseLScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMarketTheme {
                Test2()
            }
        }
    }
}
@Composable
fun Test2() {
    val context = LocalContext.current
    Column() {
        Box() {
            Row(
                modifier = Modifier
                    .padding(top = 45.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.padding(start = 18.dp),
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

        Column {
            Row(modifier = Modifier.padding(start = 18.dp),
            verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        //.background(Color.Green, shape = RoundedCornerShape(6.dp))
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(6.dp))
                ){
                    Text(
                        text = "X",
                        color = Color.White,

                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text("Café", modifier=Modifier.padding(top = 3.dp))

                Spacer(modifier = Modifier.width(250.dp))


                Box(
                    modifier = Modifier
                        .size(70.dp,40.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(25.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.8f)
                            .background(Color.White, shape = RoundedCornerShape(25.dp))
                            .padding(4.dp) // un peu de padding pour le TextField
                    ) {

                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMarketTheme {
        Test2()
    }
}