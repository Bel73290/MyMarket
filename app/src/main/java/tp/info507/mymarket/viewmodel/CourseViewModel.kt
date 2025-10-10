package td.info507.mymarket.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import td.info507.mymarket.modele.Article
import td.info507.mymarket.modele.Course
import td.info507.mymarket.storage.CourseDataBaseStorage

class CourseViewModel(context: Context) : ViewModel() {

    private val storage = CourseDataBaseStorage(context)
    val course = mutableStateOf<Course?>(null)
    val articles = mutableStateListOf<Article>()

    fun loadCourse(id: Int) {
        val result = storage.findById(id) ?: return
        course.value = result.first
        articles.clear()
        articles.addAll(result.second)
    }


    fun addArticle(name: String, priceEstime: Int) {
        val newItem = Article(
            name = name,
            price_estime = priceEstime,
            price_final = priceEstime,
            checked = false
        )
        articles.add(newItem)
        save()
    }

    fun toggle(index: Int) {
        val a = articles[index]
        articles[index] = a.copy(checked = !a.checked)
        save()
    }

    fun setFinalPrice(index: Int, newPrice: Int) {
        val a = articles[index]
        articles[index] = a.copy(price_final = newPrice)
        save()
    }

    private fun save() {
        course.value?.let { c -> storage.update(c, articles.toList()) }
    }

    fun finish() {
        val finalized = articles.map { if (it.checked) it else it.copy(price_final = 0) }
        val c = course.value ?: return
        val updated = c.copy(etat = true)
        storage.update(updated, finalized)
        course.value = updated
        articles.clear()
        articles.addAll(finalized)
    }

    fun reopen() {
        val c = course.value ?: return
        val reopened = c.copy(etat = false)
        storage.update(reopened, articles.toList())
        course.value = reopened
    }

    fun budgetFinal(): Int =
        if (course.value?.etat == true)
            articles.sumOf { it.price_final }
        else
            articles.filter { it.checked }.sumOf { it.price_final }
}
