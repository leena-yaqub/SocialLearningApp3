package com.example.sociallearningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sociallearningapp.data.model.Quiz
import com.example.sociallearningapp.data.model.QuizQuestion
import com.example.sociallearningapp.data.model.QuizResult
import com.example.sociallearningapp.repository.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _currentQuiz = MutableStateFlow<Quiz?>(null)
    val currentQuiz: StateFlow<Quiz?> = _currentQuiz.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _userAnswers = MutableStateFlow<List<Int>>(emptyList())
    val userAnswers: StateFlow<List<Int>> = _userAnswers.asStateFlow()

    private val _timeRemaining = MutableStateFlow(10)
    val timeRemaining: StateFlow<Int> = _timeRemaining.asStateFlow()

    private val _isQuizCompleted = MutableStateFlow(false)
    val isQuizCompleted: StateFlow<Boolean> = _isQuizCompleted.asStateFlow()

    val availableQuizzes: Flow<List<Quiz>> = repository.getAllQuizzes()

    fun startQuiz(quizId: Long) {
        viewModelScope.launch {
            val quiz = repository.getQuizById(quizId)
            _currentQuiz.value = quiz
            _currentQuestionIndex.value = 0
            _userAnswers.value = emptyList()
            _timeRemaining.value = 10
            _isQuizCompleted.value = false
        }
    }

    fun submitAnswer(selectedOption: Int) {
        val currentAnswers = _userAnswers.value.toMutableList()

        // Fill any skipped questions with -1
        while (currentAnswers.size <= _currentQuestionIndex.value) {
            currentAnswers.add(-1)
        }

        currentAnswers[_currentQuestionIndex.value] = selectedOption
        _userAnswers.value = currentAnswers

        nextQuestion()
    }

    fun skipQuestion() {
        val currentAnswers = _userAnswers.value.toMutableList()
        while (currentAnswers.size <= _currentQuestionIndex.value) {
            currentAnswers.add(-1)
        }
        currentAnswers[_currentQuestionIndex.value] = -1 // -1 indicates no answer
        _userAnswers.value = currentAnswers

        nextQuestion()
    }

    private fun nextQuestion() {
        val quiz = _currentQuiz.value ?: return
        val questions = quiz.getQuestions()

        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value += 1
            _timeRemaining.value = 10
        } else {
            completeQuiz()
        }
    }

    private fun completeQuiz() {
        _isQuizCompleted.value = true

        // Calculate score and save result
        val quiz = _currentQuiz.value ?: return
        val questions = quiz.getQuestions()
        val answers = _userAnswers.value

        var correctAnswers = 0
        for (i in questions.indices) {
            if (i < answers.size && answers[i] == questions[i].correctAnswer) {
                correctAnswers++
            }
        }

        val userId = auth.currentUser?.uid ?: return
        val result = QuizResult(
            userId = userId,
            quizId = quiz.id,
            quizName = quiz.name,
            score = correctAnswers,
            totalQuestions = questions.size,
            timeSpent = (questions.size * 10 - _timeRemaining.value) * 1000L // approximate
        )

        viewModelScope.launch {
            repository.saveQuizResult(result)
        }
    }

    fun getCurrentQuestion(): QuizQuestion? {
        val quiz = _currentQuiz.value ?: return null
        val questions = quiz.getQuestions()
        val index = _currentQuestionIndex.value
        return if (index < questions.size) questions[index] else null
    }

    fun getScore(): Int {
        val quiz = _currentQuiz.value ?: return 0
        val questions = quiz.getQuestions()
        val answers = _userAnswers.value

        var correctAnswers = 0
        for (i in questions.indices) {
            if (i < answers.size && answers[i] == questions[i].correctAnswer) {
                correctAnswers++
            }
        }
        return correctAnswers
    }
}
