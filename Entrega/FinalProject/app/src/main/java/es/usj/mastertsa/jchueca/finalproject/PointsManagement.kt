package es.usj.mastertsa.jchueca.finalproject

import android.os.Environment

class PointsManagement {

    public var totalPoints : Int = 0

    private val path = Environment.getExternalStorageDirectory()

    private constructor() {
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
        private lateinit var instance: PointsManagement

        public fun getInstance(): PointsManagement {

            if (instance == null) {
                instance = PointsManagement()
            }

            return instance;
        }

    }

}