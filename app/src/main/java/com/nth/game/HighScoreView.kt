package com.nth.game

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HighScoreView : SurfaceView ,Runnable {

    var screenX:Float
    var screenY : Float
    private var background: Background
    private var paint: Paint
    private var activity: GameActivity
    private lateinit var thread: Thread
    private var isPlaying = true
    private var highScores:MutableList<Int>
    private var prefs: SharedPreferences

    constructor(activity: GameActivity, screenX: Float, screenY: Float) : super(activity) {
        this.activity = activity
        this.screenX = screenX
        this.screenY = screenY
        prefs = activity.getSharedPreferences("GamePlanes", Context.MODE_PRIVATE)
        highScores = mutableListOf()
        val scoreTemp = prefs.getString("highScore"," [0,0,0,0,0,0]")
        if (scoreTemp != null && scoreTemp != ""){
            val typeScore = object : TypeToken<MutableList<Int>>() {}.type
            highScores = Gson().fromJson(scoreTemp, typeScore)
        }
        background = Background(screenX, screenY, resources)
        paint = Paint()
        paint.textSize = 128f
        paint.color = Color.BLUE

    }


    private fun draw(){
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(background.background, background.x, background.y, paint)
            var yS = 300f
            for (i in 0..5){
                canvas.drawText("${highScores[i]} score", screenX / 2f, yS, paint)
                yS += 120
            }
            isPlaying = false
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun run() {
        while (isPlaying){
            draw()
            Thread.sleep(20)
        }
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread.start()
    }

    fun pause() {
        try {
            isPlaying = false
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}