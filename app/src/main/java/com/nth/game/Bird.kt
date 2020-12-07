package com.nth.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.nth.game.GameView.Companion.screenRatioX
import com.nth.game.GameView.Companion.screenRatioY

/**
 * Created by NguyenTienHoa on 12/7/2020
 */

class Bird {

    var speed = 500
    var wasShot = true
    var x = 0
    var y = 0
    var width:Int = 0
    var height:Int = 0
    var birdCounter:Int = 1
    var bird1: Bitmap? = null
    var bird2:Bitmap? = null
    var bird3:Bitmap? = null
    var bird4:Bitmap? = null

    constructor(res: Resources){
        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1)
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2)
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3)
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4)

        width = bird1!!.width
        height = bird1!!.height

        width /= 6
        height /= 6

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        bird1 = Bitmap.createScaledBitmap(bird1!!, width, height, false)
        bird2 = Bitmap.createScaledBitmap(bird2!!, width, height, false)
        bird3 = Bitmap.createScaledBitmap(bird3!!, width, height, false)
        bird4 = Bitmap.createScaledBitmap(bird4!!, width, height, false)

        y = -height
    }

    fun getBird():Bitmap {
        if (birdCounter == 1) {
            birdCounter++
            return bird1!!
        }

        if (birdCounter == 2) {
            birdCounter++
            return bird2!!
        }

        if (birdCounter == 3) {
            birdCounter++
            return bird3!!
        }

        birdCounter = 1

        return bird4!!
    }

    fun getCollisionShape(): Rect{
        return Rect(x, y, x + width, y + height)
    }

}