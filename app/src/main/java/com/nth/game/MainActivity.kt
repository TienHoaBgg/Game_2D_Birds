package com.nth.game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nth.game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isMute = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnPlay.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    GameActivity::class.java
                )
            )
        }

        val prefs = getSharedPreferences("GamePlanes", MODE_PRIVATE)
        binding.txtHighScore.text = "HighScore: " + prefs.getInt("highscore", 0)
        isMute = prefs.getBoolean("isMute", false)

        if (isMute) {
            binding.btnVolume.setImageResource(R.drawable.ic_volume_off_black_24dp)
        } else {
            binding.btnVolume.setImageResource(R.drawable.ic_volume_up_black_24dp)
        }
        binding.btnVolume.setOnClickListener {
            isMute = !isMute
            if (isMute) {
                binding.btnVolume.setImageResource(R.drawable.ic_volume_off_black_24dp)
            } else {
                binding.btnVolume.setImageResource(
                    R.drawable.ic_volume_up_black_24dp
                )
            }
            val editor = prefs.edit()
            editor.putBoolean("isMute", isMute)
            editor.apply()
        }
    }
}