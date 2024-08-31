package bhuwan.example.stopwatch

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import bhuwan.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isRunning: Boolean = false
    private var isPaused: Boolean = false
    private var elapsedTime: Long = 0
    private var startTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.isEnabled = true
        binding.pauseButton.isEnabled = false
        binding.resetButton.isEnabled = false

        binding.startButton.setOnClickListener {
            startTimer()
        }

        binding.pauseButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else if (isPaused) {
                resumeTimer()
            }
        }

        binding.resetButton.setOnClickListener {
            resetTimer()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updateTimerTextView(timeInMillis: Long) {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        val milliseconds = (timeInMillis % 1000) / 10
        binding.timerTextView.text = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            elapsedTime = System.currentTimeMillis() - startTime
            updateTimerTextView(elapsedTime)
            handler.postDelayed(this, 30)
        }
    }

    private fun startTimer() {
        if (!isRunning && !isPaused) {
            startTime = System.currentTimeMillis()
            handler.post(runnable)
            isRunning = true
            binding.startButton.isEnabled = false
            binding.pauseButton.isEnabled = true
            binding.pauseButton.text = "Pause"
            binding.resetButton.isEnabled = true
        }
    }

    private fun resumeTimer() {
        if (!isRunning && isPaused) {
            startTime = System.currentTimeMillis() - elapsedTime
            handler.post(runnable)
            isRunning = true
            isPaused = false
            binding.pauseButton.text = "Pause"
            binding.resetButton.isEnabled = true
        }
    }

    private fun pauseTimer() {
        if (isRunning) {
            elapsedTime = System.currentTimeMillis() - startTime
            handler.removeCallbacks(runnable)
            isRunning = false
            isPaused = true
            binding.pauseButton.text = "Resume"
        }
    }

    private fun resetTimer() {
        handler.removeCallbacks(runnable)
        isRunning = false
        isPaused = false
        elapsedTime = 0
        updateTimerTextView(0)
        binding.startButton.isEnabled = true
        binding.pauseButton.isEnabled = false
        binding.resetButton.isEnabled = false
    }
}
