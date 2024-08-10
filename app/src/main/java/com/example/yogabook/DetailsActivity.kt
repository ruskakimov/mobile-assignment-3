package com.example.yogabook

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Timer
import java.util.TimerTask

class DetailsActivity : AppCompatActivity() {
    // Timer state
    val timer = Timer()
    var elapsedSeconds = 0

    // Intent data
    var poseName = ""
    var imageResId = 0
    var maxDuration = 10

    // UI elements
    lateinit var toolbar: Toolbar
    lateinit var titleTextView: TextView
    lateinit var difficultyTextView: TextView
    lateinit var durationTextView: TextView
    lateinit var poseImageView: ImageView
    lateinit var playButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve data from the intent
        poseName = intent.getStringExtra("poseName") ?: "?"
        imageResId = intent.getIntExtra("poseImageResId",0)

        // UI elements
        toolbar = findViewById(R.id.toolbar)
        titleTextView = findViewById(R.id.title)
        difficultyTextView = findViewById(R.id.difficulty)
        durationTextView = findViewById(R.id.duration)
        poseImageView = findViewById(R.id.pose_image)
        playButton = findViewById(R.id.imageButton)

        // Set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Update UI data
        titleTextView.text = poseName
        poseImageView.setImageResource(imageResId)
        playButton.setOnClickListener { onPlayButtonClick() }
    }

    private fun onPlayButtonClick() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                elapsedSeconds++
                runOnUiThread {
                    // Update the UI here
                    durationTextView.text = "Time left – ${maxDuration - elapsedSeconds} sec"
                }
                if (elapsedSeconds >= maxDuration) {
                    timer.cancel()
                }
            }
        }, 0, 1000)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}