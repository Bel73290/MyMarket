//package td.info507.mymarket.viewmodel
//
//import android.content.Context
//import androidx.compose.runtime.mutableStateListOf
//import androidx.lifecycle.ViewModel
//import td.info507.mymarket.modele.Course
//import td.info507.mymarket.storage.CourseDataBaseStorage
//
//class CoursesListViewModel(context: Context) : ViewModel() {
//
//    private val storage = CourseDataBaseStorage(context)
//
//    val courses = mutableStateListOf<Course>()
//
//    fun loadAll() {
//        courses.clear()
//        courses.addAll(storage.getAllCourses())
//    }
//
//    fun getGroupedCourses(): Pair<List<Course>, List<Course>> {
//        val inProgress = courses.filter { !it.etat }
//        val finished = courses.filter { it.etat }
//        return Pair(inProgress, finished)
//    }
//}
