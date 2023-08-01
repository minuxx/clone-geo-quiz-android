package com.minux.geoquiz

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var getResultFromCheat: ActivityResultLauncher<Intent>

    // by lazy 키워드를 사용하면 최초로 quizViewModel 이 사용될 때까지 초기화를 늦출 수 있다.
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle?) called")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        previousButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        getResultFromCheat = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val questionIdx = it.data?.getIntExtra(EXTRA_QUESTION_IDX, -1) ?: -1
                val isCheat = it.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
                if (isCheat && questionIdx != -1) {
                    quizViewModel.changeQuestionIsCheatTrue(questionIdx)
                    cheatButton.isEnabled = quizViewModel.isOkCheat
                }
            }
        }

        cheatButton.setOnClickListener {
            val questionIdx = quizViewModel.currentIndex
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, questionIdx, answerIsTrue)
            getResultFromCheat.launch(intent)
        }

        updateQuestion()
        cheatButton.isEnabled = quizViewModel.isOkCheat
    }

    private fun updateQuestion() {
//        Log.d(TAG, "Updating question text", Exception())
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        changeButtonState()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        quizViewModel.saveUserAnswer(userAnswer)
        changeButtonState()

        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.currentQuestionIsCheat -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else ->R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (quizViewModel.isAllAnswer) {
            val message = "${getString(R.string.correct_percentage)} ${quizViewModel.correctPercentage}%"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeButtonState() {
        trueButton.isEnabled = !quizViewModel.isAnswer
        falseButton.isEnabled = !quizViewModel.isAnswer
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}