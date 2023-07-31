package com.minux.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val isAnswer: Boolean
        get() = questionBank[currentIndex].userAnswer != null

    val isAllAnswer: Boolean
        get() = questionBank.filter { it.userAnswer != null }.size == questionBank.size

    val correctPercentage: Int
        get() {
            val correctAnswerSize = questionBank.filter { it.userAnswer != null && it.userAnswer == it.answer }.size
            return ((correctAnswerSize.toDouble() / questionBank.size) * 100).toInt()
        }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = if (currentIndex == 0) {
            questionBank.size - 1
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }

    fun saveUserAnswer(userAnswer: Boolean) {
        questionBank[currentIndex].userAnswer = userAnswer
    }
}