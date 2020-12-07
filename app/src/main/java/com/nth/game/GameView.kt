package com.nth.game

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceView
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by NguyenTienHoa on 12/7/2020
 */

class GameView : SurfaceView, Runnable {

    companion object {
        var screenRatioX = 0f
        var screenRatioY = 0f
    }

    private lateinit var thread: Thread
    private var isPlaying = false
    private var isGameOver = false
    private var screenX: Float
    private var screenY: Float
    private var score = 0
    private var paint: Paint
    private var birds: MutableList<Bird>
    private var prefs: SharedPreferences
    private var random: Random
    private var soundPool: SoundPool
    private var bullets: MutableList<Bullet>
    private var sound = 0
    private var flight: Flight
    private var activity: GameActivity
    private var background1: Background
    private var background2: Background

    constructor(activity: GameActivity, screenX: Float, screenY: Float) : super(activity) {
        this.activity = activity
        prefs = activity.getSharedPreferences("GamePlanes", Context.MODE_PRIVATE)
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else SoundPool(1, AudioManager.STREAM_MUSIC, 0)

        sound = soundPool.load(activity, R.raw.shoot, 1)
        this.screenX = screenX
        this.screenY = screenY

        screenRatioX = 1920f / screenX
        screenRatioY = 1080f / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)
        flight = Flight(this, screenY, resources)
        bullets = ArrayList()
        background2.x = screenX
        paint = Paint()
        paint.textSize = 128f
        paint.color = Color.WHITE
        birds = mutableListOf()
        for (i in 0..3) {
            val bird = Bird(resources)
            birds.add(bird)
        }
        random = Random()
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> if (event.x < screenX / 2) {
                flight.isGoingUp = true
            }
            MotionEvent.ACTION_UP -> {
                flight.isGoingUp = false
                if (event.x > screenX / 2) flight.toShoot++
            }
        }
        return true
    }


    private fun update() {
        background1.x -= (10 * screenRatioX).toInt()
        background2.x -= (10 * screenRatioX).toInt()
        if (background1.x + background1.background.width < 0) {
            background1.x = screenX
        }
        if (background2.x + background2.background.width < 0) {
            background2.x = screenX
        }
        if (flight.isGoingUp) {
            flight.y -= (30 * screenRatioY).toInt()
        } else {
            flight.y += (30 * screenRatioY).toInt()
        }
        if (flight.y < 0)
            flight.y = 0F
        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height

        val trash: MutableList<Bullet> = arrayListOf()
        for (bullet in bullets) {
            if (bullet.x > screenX) trash.add(bullet)
            bullet.x += (50 * screenRatioX).toInt()
            for (bird in birds) {
                if (Rect.intersects(
                        bird.getCollisionShape(),
                        bullet.getCollisionShape()
                    )
                ) {
                    score++
                    bird.x = -500
                    bullet.x = screenX + 500
                    bird.wasShot = true
                }
            }
        }
        for (bullet in trash) bullets.remove(bullet)
        for (bird in birds) {
            bird.x -= bird.speed
            if (bird.x + bird.width < 0) {
                if (!bird.wasShot) {
                    isGameOver = true
                    return
                }
                val bound = (30 * screenRatioX).toInt()
                bird.speed = random.nextInt(bound)
                if (bird.speed < 10 * screenRatioX) bird.speed =
                    (10 * screenRatioX).toInt()
                bird.x = screenX.toInt()
                bird.y = random.nextInt((screenY - bird.height).toInt())
                bird.wasShot = false
            }
            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true
                return
            }
        }
    }

    private fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint)
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint)
            for (bird in birds) canvas.drawBitmap(
                bird.getBird(),
                bird.x.toFloat(),
                bird.y.toFloat(),
                paint
            )
            canvas.drawText(score.toString() + "", screenX / 2f, 164f, paint)
            if (isGameOver) {
                isPlaying = false
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint)
                holder.unlockCanvasAndPost(canvas)
                saveIfHighScore()
                waitBeforeExiting()
                return
            }
            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint)
            for (bullet in bullets) canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun waitBeforeExiting() {
        try {
            Thread.sleep(3000)
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            val editor = prefs.edit()
            editor.putInt("highscore", score)
            editor.apply()
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(80)
        } catch (e: InterruptedException) {
            e.printStackTrace()
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

    fun newBullet() {
        if (!prefs.getBoolean("isMute", false)) soundPool.play(sound, 1f, 1f, 0, 0, 1f)
        val bullet = Bullet(resources)
        bullet.x = flight.x + flight.width
        bullet.y = flight.y + flight.height / 2
        bullets.add(bullet)
    }

}