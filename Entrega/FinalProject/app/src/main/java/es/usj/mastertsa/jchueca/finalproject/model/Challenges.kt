package es.usj.mastertsa.jchueca.finalproject.model

object Challenges {

    var challenges : MutableList<Challenge> = mutableListOf()

    init {

    }

    fun get(index: Int) : Challenge {
        return challenges[index]
    }
}