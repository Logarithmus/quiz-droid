package dev.logarithmus.quizdroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dev.logarithmus.quizdroid.databinding.ActivityMainBinding
import dev.logarithmus.quizdroid.dialogs.ExitDialogFragment
import dev.logarithmus.quizdroid.dialogs.FinishDialogFragment
import dev.logarithmus.quizdroid.dialogs.FirebaseFailedDialogFragment
import dev.logarithmus.quizdroid.dialogs.ResultsDialogFragment
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class MainActivity: AppCompatActivity(),
    FinishDialogFragment.FinishDialogListener {

    @Serializable
    class Question(
        var question: String = "",
        var answers: MutableList<String> = mutableListOf(""),
        var correctAnswerIndex: Int = 0
    ) {
        fun shuffleAnswers() {
            answers = answers.mapIndexed { i, ans -> Pair(i, ans) }.shuffled().also {
                correctAnswerIndex = it.indexOfFirst { it.first == correctAnswerIndex }
            }.map { it.second }.toMutableList()
        }
    }
    
    private lateinit var activity: ActivityMainBinding
    private var questionIndex = 0
    private var questionCount = 1
    private lateinit var questions: MutableList<Question>
    private lateinit var userAnswers: MutableList<Int?>
    private var isReviewMode = false
    private lateinit var mInterstitialAd: InterstitialAd
    
    private fun View.setVisibility(f: Boolean) {
        visibility = if (f) { View.VISIBLE } else { View.INVISIBLE }
    }

    private var RadioGroup.checkedIndex: Int?
        get() = indexOfChild(findViewById(checkedRadioButtonId))
        set(index) {
            index?.let{
                (getChildAt(index) as RadioButton).isChecked = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activity.root)

        MobileAds.initialize(this) {}
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.ad_unit_id)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        //questions = loadQuestionsFromFile("quiz.json")
        loadQuestionsFromFirebase(getString(R.string.firebase_url))
    }
    
    private fun onQuestionsFromFirebaseLoaded(questions: List<Question>?) {
        Log.e("[FIREBASE]", questions.toString())
        questions?.let {
            this.questions = it.toMutableList()
            questionCount = this.questions.size
            userAnswers = MutableList(questionCount) { null }
            shuffleQuestions(this.questions)
            showCurrentQuestion()
            activity.answersRadioGroup.setOnCheckedChangeListener { group, _ ->
                userAnswers[questionIndex] = group.checkedIndex
            }
        } ?: FirebaseFailedDialogFragment().show(supportFragmentManager, "FirebaseFailedDialog")
    }

    private fun loadQuestionsFromFile(name: String): List<Question> {
        val json = File(applicationContext.getExternalFilesDir(null), name).readText()
        return Json.decodeFromString<ArrayList<Question>>(json)
    }

    private fun loadQuestionsFromFirebase(url: String) {
        Firebase.database(url).reference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("[FIREBASE]", snapshot.getValue<List<Any>>().toString())
                Log.e("[FIREBASE]", snapshot.getValue<List<Question>>().toString())
                onQuestionsFromFirebaseLoaded(snapshot.getValue<List<Question>>())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("[FIREBASE]", "loadQuestions:onCancelled", error.toException())
            }
        })
    }

    private fun shuffleQuestions(questions: MutableList<Question>) {
        questions.apply {
            shuffle()
            forEach{ it.shuffleAnswers() }
        }
    }
    
    @SuppressLint("SetTextI18n")
    private fun showCurrentQuestion() {
        val question = questions[questionIndex]
        activity.apply {
            questionCountView.text = "${questionIndex + 1}/${questionCount}"
            questionText.text = question.question
            answersRadioGroup.removeAllViews()
            question.answers.forEach{
                answersRadioGroup.addView(
                    RadioButton(this@MainActivity).apply {
                        text = it
                        isClickable = !isReviewMode
                    }
                )
            }
            answersRadioGroup.checkedIndex = userAnswers[questionIndex]
            if (isReviewMode) {
                highlightAnswers()
            }
        }
        updateButtonsState()
    }

    private fun updateButtonsState() {
        activity.prevButton.setVisibility(questionIndex > 0)
        activity.nextButton.setVisibility(questionIndex + 1 < questionCount)
    }

    private fun highlightAnswers() {
        userAnswers[questionIndex]?.let{
            val incorrectView = activity.answersRadioGroup.getChildAt(it)
            (incorrectView as RadioButton).setTextColor(0xFFC40233.toInt()) // NCS Red
        }
        val question = questions[questionIndex]
        val correctView = activity.answersRadioGroup.getChildAt(question.correctAnswerIndex)
        (correctView as RadioButton).setTextColor(0xFF009F6B.toInt()) // NCS Green
    }
    
    fun onClickPrevQuestion(@Suppress("UNUSED_PARAMETER") view: View) {
        questionIndex -= 1
        showCurrentQuestion()
    }

    fun onClickNextQuestion(@Suppress("UNUSED_PARAMETER") view: View) {
        questionIndex += 1
        showCurrentQuestion()
    }

    fun onClickExit(@Suppress("UNUSED_PARAMETER") view: View) {
        ExitDialogFragment().show(supportFragmentManager, "ExitDialog")
    }

    fun onClickFinish(@Suppress("UNUSED_PARAMETER") view: View) {
        FinishDialogFragment().show(supportFragmentManager, "FinishDialog")
    }

    override fun finishQuiz() {
        val correctCount = userAnswers.zip(questions).count {
            it.first == it.second.correctAnswerIndex
        }
        ResultsDialogFragment(correctCount, questionCount)
                .show(supportFragmentManager, "ResultsDialog")
        activity.finishButton.text = "Restart"
        activity.finishButton.setOnClickListener { recreate() }
        isReviewMode = true
        questionIndex = 0
        showCurrentQuestion()
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Log.d("[ADS]", "The interstitial wasn't loaded yet.")
        }
    }
}