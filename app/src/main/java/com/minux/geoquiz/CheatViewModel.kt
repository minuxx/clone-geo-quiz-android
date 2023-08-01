package com.minux.geoquiz

import androidx.lifecycle.ViewModel

class CheatViewModel : ViewModel() {
    var questionIdx = -1
    var answerIsTrue = false
    var isAnswerShown = false
}