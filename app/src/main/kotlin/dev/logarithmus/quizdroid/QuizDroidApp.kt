package dev.logarithmus.quizdroid

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class QuizDroidApp: Application() {
    lateinit var db: FirebaseDatabase

    override fun onCreate() {
        super.onCreate()
        db = Firebase.database(getString(R.string.firebase_url))
        db.setPersistenceEnabled(true)
    }
}