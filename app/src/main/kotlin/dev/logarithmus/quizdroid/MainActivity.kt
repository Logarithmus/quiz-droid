package dev.logarithmus.quizdroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.logarithmus.quizdroid.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private lateinit var activity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activity.root)
    }
}