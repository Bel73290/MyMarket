package tp.info507.mymarket.GET

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import kotlin.random.Random

suspend fun getConseil(): String {
    return try {
        val jsonText = withContext(Dispatchers.IO) {
            URL("http://51.68.91.213/gr-1-6/conseils.json").readText()
        }
        val arr = JSONObject(jsonText).getJSONArray("conseils")
        val i = Random.nextInt(arr.length())
        arr.getJSONObject(i).getString("description")
    } catch (e: Exception) {
        "Erreur de récupération du conseil"
    }
}



