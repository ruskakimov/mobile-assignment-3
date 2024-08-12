package com.example.yogabook

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Timer
import java.util.TimerTask

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "timer_channel"
    }

    // Timer state
    var timer: Timer? = null
    var elapsedSeconds = 0

    // Intent data
    var poseName = ""
    var imageResId = 0
    var difficulty = Difficulty.EASY
    var maxDuration = 10

    // UI elements
    lateinit var toolbar: Toolbar
    lateinit var titleTextView: TextView
    lateinit var difficultyTextView: TextView
    lateinit var durationTextView: TextView
    lateinit var poseImageView: ImageView
    lateinit var playButton: ImageButton
    lateinit var progressBar: ProgressBar

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
        difficulty = Difficulty.valueOf(intent.getStringExtra("difficulty") ?: "EASY")
        maxDuration = intent.getIntExtra("durationSeconds", 10)

        // UI elements
        toolbar = findViewById(R.id.toolbar)
        titleTextView = findViewById(R.id.title)
        difficultyTextView = findViewById(R.id.difficulty)
        durationTextView = findViewById(R.id.duration)
        poseImageView = findViewById(R.id.pose_image)
        playButton = findViewById(R.id.imageButton)
        progressBar = findViewById(R.id.toolbar_progress)

        // Set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Update UI data
        titleTextView.text = poseName
        poseImageView.setImageResource(imageResId)
        difficultyTextView.text = "Difficulty – ${difficulty.name.lowercase().replaceFirstChar { it.uppercase() }}"
        durationTextView.text = getDurationString()
        playButton.setOnClickListener { onPlayButtonClick() }
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanUpTimer()
    }

    private fun getDurationString(): String {
        return "Duration – ${getTimeString(maxDuration)}"
    }

    private fun getTimerStateString(): String {
        return "Time left – ${getTimeString(maxDuration - elapsedSeconds)}"
    }

    private fun getTimeString(seconds: Int): String {
        return if (seconds < 60) {
            "$seconds sec"
        } else {
            val M = seconds / 60
            val S = seconds % 60

            if (S == 0) {
                "$M min"
            } else {
                "$M min $S sec"
            }
        }
    }

    private fun cleanUpTimer() {
        timer?.cancel()
        timer = null
        elapsedSeconds = 0
        playButton.setImageResource(R.drawable.baseline_play_arrow_24)
        durationTextView.text = getDurationString()
        progressBar.progress = 0
        progressBar.visibility = ProgressBar.GONE
    }

    private fun onPlayButtonClick() {
        requestNotificationPermission()

        // Stop
        if (timer != null) {
            cleanUpTimer()
            return
        }

        cleanUpTimer()

        // Update UI
        progressBar.visibility = ProgressBar.VISIBLE
        playButton.setImageResource(R.drawable.baseline_stop_24)

        timer = Timer().also {
            it.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        // Update the UI here
                        durationTextView.text = if (elapsedSeconds < maxDuration) getTimerStateString() else getDurationString()
                        progressBar.progress = (elapsedSeconds * 100) / maxDuration
                    }
                    if (elapsedSeconds >= maxDuration) {
                        cleanUpTimer()
                        showNotification()
                    }
                    elapsedSeconds++
                }
            }, 0, 1000)
        }
    }

    private fun showNotification() {
        // Create the notification channel (only needed for Android 8.0+)
        createNotificationChannel()

        // Build the notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setContentTitle("Pose Complete.")
            .setContentText("You can rest now or start another pose!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            with(NotificationManagerCompat.from(this)) {
                notify(1, builder.build())
            }
        }
    }

    private fun requestNotificationPermission() {
        val hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Timer Alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = "Your Channel Description"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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