package com.example.sociallearningapp.data

import com.example.sociallearningapp.data.model.Quiz
import com.example.sociallearningapp.data.model.QuizQuestion

object SampleQuizData {

    fun getGeneralKnowledgeQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            question = "What is the capital of France?",
            option1 = "London",
            option2 = "Berlin",
            option3 = "Paris",
            option4 = "Madrid",
            correctAnswer = 2,
            explanation = "Paris is the capital and largest city of France."
        ),
        QuizQuestion(
            question = "Which planet is known as the Red Planet?",
            option1 = "Venus",
            option2 = "Mars",
            option3 = "Jupiter",
            option4 = "Saturn",
            correctAnswer = 1,
            explanation = "Mars appears red due to iron oxide (rust) on its surface."
        ),
        QuizQuestion(
            question = "What is 12 × 8?",
            option1 = "96",
            option2 = "88",
            option3 = "104",
            option4 = "92",
            correctAnswer = 0,
            explanation = "12 multiplied by 8 equals 96."
        ),
        QuizQuestion(
            question = "Who wrote 'Romeo and Juliet'?",
            option1 = "Charles Dickens",
            option2 = "William Shakespeare",
            option3 = "Jane Austen",
            option4 = "Mark Twain",
            correctAnswer = 1,
            explanation = "William Shakespeare wrote this famous tragedy in the 1590s."
        ),
        QuizQuestion(
            question = "What is the largest ocean on Earth?",
            option1 = "Atlantic Ocean",
            option2 = "Indian Ocean",
            option3 = "Arctic Ocean",
            option4 = "Pacific Ocean",
            correctAnswer = 3,
            explanation = "The Pacific Ocean covers about 46% of Earth's water surface."
        )
    )

    fun getMathQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            question = "What is the square root of 64?",
            option1 = "6",
            option2 = "8",
            option3 = "10",
            option4 = "12",
            correctAnswer = 1
        ),
        QuizQuestion(
            question = "If x + 5 = 12, what is x?",
            option1 = "5",
            option2 = "7",
            option3 = "17",
            option4 = "6",
            correctAnswer = 1
        ),
        QuizQuestion(
            question = "What is 15% of 200?",
            option1 = "25",
            option2 = "30",
            option3 = "35",
            option4 = "40",
            correctAnswer = 1
        ),
        QuizQuestion(
            question = "How many sides does a hexagon have?",
            option1 = "5",
            option2 = "6",
            option3 = "7",
            option4 = "8",
            correctAnswer = 1
        ),
        QuizQuestion(
            question = "What is π (pi) approximately equal to?",
            option1 = "3.14",
            option2 = "2.71",
            option3 = "1.61",
            option4 = "4.20",
            correctAnswer = 0
        )
    )

    fun getScienceQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            question = "What is the chemical symbol for water?",
            option1 = "H2O",
            option2 = "CO2",
            option3 = "NaCl",
            option4 = "O2",
            correctAnswer = 0
        ),
        QuizQuestion(
            question = "How many bones are in the human body?",
            option1 = "195",
            option2 = "206",
            option3 = "220",
            option4 = "186",
            correctAnswer = 1
        ),
        QuizQuestion(
            question = "What gas do plants absorb from the atmosphere?",
            option1 = "Oxygen",
            option2 = "Nitrogen",
            option3 = "Carbon Dioxide",
            option4 = "Hydrogen",
            correctAnswer = 2
        ),
        QuizQuestion(
            question = "What is the speed of light?",
            option1 = "300,000 km/s",
            option2 = "299,792,458 m/s",
            option3 = "150,000 km/s",
            option4 = "500,000 m/s",
            correctAnswer = 1
        ),
        QuizQuestion(
            question = "What is the hardest natural substance?",
            option1 = "Gold",
            option2 = "Iron",
            option3 = "Diamond",
            option4 = "Quartz",
            correctAnswer = 2
        )
    )

    fun getAllSampleQuizzes(): List<Quiz> = listOf(
        Quiz.fromQuestions(
            name = "General Knowledge Quiz",
            description = "Test your general knowledge with these questions",
            questions = getGeneralKnowledgeQuestions(),
            category = "General Knowledge"
        ),
        Quiz.fromQuestions(
            name = "Math Challenge",
            description = "Basic mathematics problems",
            questions = getMathQuestions(),
            category = "Mathematics"
        ),
        Quiz.fromQuestions(
            name = "Science Quiz",
            description = "Explore the world of science",
            questions = getScienceQuestions(),
            category = "Science"
        )
    )
}