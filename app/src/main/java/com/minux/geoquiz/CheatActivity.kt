package com.minux.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

const val EXTRA_QUESTION_IDX = "com.minux.geoquiz.question_idx"
private const val EXTRA_ANSWER_IS_TRUE = "com.minux.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.minux.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private val cheatViewModel: CheatViewModel by lazy {
        ViewModelProvider(this)[CheatViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        cheatViewModel.questionIdx = intent.getIntExtra(EXTRA_QUESTION_IDX, -1)
        cheatViewModel.answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            setAnswerTextView()
            setAnswerShownResult()
        }

        if (cheatViewModel.isAnswerShown) {
            setAnswerTextView()
            setAnswerShownResult()
        }
    }

    private fun setAnswerTextView() {
        val answerText = when {
            cheatViewModel.answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
    }

    private fun setAnswerShownResult() {
        cheatViewModel.isAnswerShown = true
        val data = Intent().apply {
            putExtra(EXTRA_QUESTION_IDX, cheatViewModel.questionIdx)
            putExtra(EXTRA_ANSWER_SHOWN, true)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, questionIdx: Int, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_QUESTION_IDX, questionIdx)
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}