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

class Flight {

    var toShoot = 0
    var isGoingUp = false
    var isDie = false
    var isGoingRight = false
    var goingTo = 0
    var x = 0f
    var y:Float = 0f
    var width:Int = 0
    var height:Int = 0
    var wingCounter:Int = 0
    var shootCounter:Int = 1
    var flight1: Bitmap
    var flight2:Bitmap
    var shoot1:Bitmap
    var shoot2:Bitmap
    var shoot3:Bitmap
    var shoot4:Bitmap
    var shoot5:Bitmap
    private var dead:Bitmap
    private var gameView: GameView

    constructor(gameView: GameView, screenY: Float, res: Resources){
        this.gameView = gameView

        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1)
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2)

        width = flight1.width
        height = flight1.height

        width /= 4
        height /= 4

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false)
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false)

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1)
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2)
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3)
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4)
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5)

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false)
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false)
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false)
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false)
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false)

        dead = BitmapFactory.decodeResource(res, R.drawable.dead)
        dead = Bitmap.createScaledBitmap(dead, width, height, false)

        y = screenY / 2
        x = (64 * screenRatioX)

    }

    fun getFlight(): Bitmap {
        if (toShoot != 0) {
            if (shootCounter == 1) {
                shootCounter++
                return shoot1
            }
            if (shootCounter == 2) {
                shootCounter++
                return shoot2
            }
            if (shootCounter == 3) {
                shootCounter++
                return shoot3
            }
            if (shootCounter == 4) {
                shootCounter++
                return shoot4
            }
            shootCounter = 1
            toShoot--
            gameView.newBullet()
            return shoot5
        }
        if (wingCounter == 0) {
            wingCounter++
            return flight1
        }
        wingCounter--
        return flight2
    }

    fun getCollisionShape(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }

    @JvmName("getDead1")
    fun getDead(): Bitmap {
        return dead
    }


}