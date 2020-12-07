package com.nth.game

import android.graphics.Point
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by NguyenTienHoa on 12/7/2020
 */

class GameActivity : AppCompatActivity() {

    private lateinit var gameView: GameView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        gameView = GameView(this, point.x.toFloat(), point.y.toFloat())
        setContentView(gameView)
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

}