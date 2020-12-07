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

class Bullet {

     var x:Float = 0f
     var y:Float = 0f
     var width:Int
     var height:Int
     var bullet: Bitmap

    constructor(res: Resources){
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet)

        width = bullet.width
        height = bullet.height

        width /= 4
        height /= 4

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false)

    }

    fun getCollisionShape(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }

}