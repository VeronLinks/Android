package es.usj.mastertsa.jchueca.session301.model

import es.usj.mastertsa.jchueca.session301.EMPTY

data class Tile (var x: Int, var y: Int, var width: Int, var content: Int = EMPTY, var shown: Boolean = false)
{
    fun isInside(newX: Int, newY: Int): Boolean
    {
        return newX >= x &&
               newX <= x + width &&
               newY >= y &&
               newY <= y + width
    }
}

