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
    private lateinit var highScoreView: HighScoreView
    private var idA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        gameView = GameView(this, point.x.toFloat(), point.y.toFloat())
        highScoreView = HighScoreView(this, point.x.toFloat(), point.y.toFloat())
        idA = intent.getIntExtra("Activity",1)
        when(idA){
            1 -> setContentView(gameView)
            2-> setContentView(highScoreView)
        }
    }

    override fun onPause() {
        super.onPause()
        when(idA){
            1 -> gameView.pause()
            2-> highScoreView.pause()
        }

    }

    override fun onResume() {
        super.onResume()
        when(idA){
            1 -> gameView.resume()
            2-> highScoreView.resume()
        }
    }

}