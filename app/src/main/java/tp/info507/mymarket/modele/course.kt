package td.info507.mymarket.modele

data class Course(
    val id: Int,
    val nom: String,
    val date: String,
    val prix_initial: Int,
    val prix_final: Int,
    val etat: Boolean
) {
    companion object {
        const val ID = "id"
        const val NOM = "nom"
        const val DATE = "date"
        const val PRIX_INITIAL = "prix_initial"
        const val PRIX_FINAL = "prix_final"
        const val ETAT = "etat"
    }
}




