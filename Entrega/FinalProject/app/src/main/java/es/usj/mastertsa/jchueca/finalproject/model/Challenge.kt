package es.usj.mastertsa.jchueca.finalproject.model

data class Challenge (
    var id: Int,
    var description: String,
    var points: Int,
    var isCompleted: Boolean,
    //var address: String,
    var latitude: Double,
    var longitude: Double){

    constructor(_id: Int) : this(_id,
                            "Accept to see your next destination",
                            100,
                            false,
                            0.0,
                            0.0)
}