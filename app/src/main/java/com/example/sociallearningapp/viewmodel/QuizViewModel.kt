package com.example.sociallearningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sociallearningapp.data.model.Quiz
import com.example.sociallearningapp.data.model.QuizQuestion
import com.example.sociallearningapp.data.model.QuizResult
import com.example.sociallearningapp.data.repository.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _currentQuiz = MutableStateFlow<Quiz?>(null)
    val currentQuiz: StateFlow<Quiz?> = _currentQuiz

    private val _questions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val questions: StateFlow<List<QuizQuestion>> = _questions

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _userAnswers = mutableListOf<Int>()

    private val _timeRemaining = MutableStateFlow(10)
    val timeRemaining: StateFlow<Int> = _timeRemaining

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished

    val availableQuizzes: Flow<List<Quiz>> = repository.getAllQuizzes()

    fun getQuizHistory(): Flow<List<QuizResult>> {
        return repository.getQuizHistory(auth.currentUser?.uid ?: "")
    }

    fun startQuiz(quizId: Long) {
        viewModelScope.launch {
            _currentQuiz.value = repository.getQuizById(quizId)
            _questions.value = repository.getQuestionsForQuiz(quizId)
            _currentQuestionIndex.value = 0
            _timeRemaining.value = 10
            _isQuizFinished.value = false
            _userAnswers.clear()
        }
    }

    fun submitAnswer(selectedOption: Int) {
        _userAnswers.add(selectedOption)
        nextQuestion()
    }

    fun skipQuestion() {
        _userAnswers.add(-1) // -1 indicates a skipped question
        nextQuestion()
    }

    private fun nextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value++
            _timeRemaining.value = 10
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        viewModelScope.launch {
            val score = calculateScore()
            val result = QuizResult(
                userId = auth.currentUser?.uid ?: "",
                quizId = _currentQuiz.value?.id ?: 0,
                quizName = _currentQuiz.value?.name ?: "",
                score = score,
                totalQuestions = _questions.value.size,
                timestamp = System.currentTimeMillis(),
                timeSpent = 0L
            )
            repository.saveQuizResult(result)
            _isQuizFinished.value = true
        }
    }

    private fun calculateScore(): Int {
        var score = 0
        for (i in _questions.value.indices) {
            if (i < _userAnswers.size && _userAnswers[i] == _questions.value[i].correctAnswer) {
                score++
            }
        }
        return score
    }

    fun getScore(): Int {
        return calculateScore()
    }

    fun decrementTime() {
        if (_timeRemaining.value > 0) {
            _timeRemaining.value--
        } else {
            skipQuestion()
        }
    }
}
