package es.usj.mastertsa.jchueca.finalproject

import android.os.Environment

class PointsManagement private constructor() {

    public var totalPoints : Int = 0

    private val path = Environment.getExternalStorageDirectory()

    init {
        load()
    }

    public fun save() {
        // TODO

    }

    private fun load() {
        // TODO

    }

    companion object {

        public val POINTS_PER_REWARD = 100
        val instance: PointsManagement = PointsManagement()

    }

}