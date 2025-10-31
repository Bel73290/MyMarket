package tp.info507.mymarket.GET

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import kotlin.random.Random

// utiliser suspend car Android interdit les requêtes réseau sur le “thread principal”
suspend fun getConseil(): String {
    return try {
        //On passe en thread IO et pas en principal
        val jsonText = withContext(Dispatchers.IO) {
            URL("http://51.68.91.213/gr-1-6/conseils.json").readText()
        }

        //recup du conseil aléatoire avec Ramdom.nextInt
        val arr = JSONObject(jsonText).getJSONArray("conseils")
        val i = Random.nextInt(arr.length())
        arr.getJSONObject(i).getString("description") //recupétation du conseil aléatoire
    } catch (e: Exception) {
        "Erreur de récupération du conseil"
    }
}



