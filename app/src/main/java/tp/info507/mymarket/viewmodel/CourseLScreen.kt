package tp.info507.mymarket.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import td.info507.mymarket.ui.theme.MyMarketTheme
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
        Box(){
            Row(modifier=Modifier
                .padding(top = 45.dp)
                .padding(start = 18.dp),
                verticalAlignment = Alignment.CenterVertically){
                IconButton(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_fleche),
                        contentDescription = "Galerie",
                        tint = Color.Gray
                    )
                }

                Text(text="Course 1",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                    )

            }

        }
    }

}
