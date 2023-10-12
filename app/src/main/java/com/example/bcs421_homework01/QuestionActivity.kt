package com.example.bcs421_homework01

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class QuestionActivity : AppCompatActivity() {
    //creates class for questions
    data class Question (
        val text: String,
        val options: List<String>,
        val correctOptionIndex: Int,
        val payout:Int
    )

    //populates question list with 7 questions
    //I would like to update this with more questions and have the questions display in a random order

    private val questions = mutableListOf(
        Question(
            "Who was introduced as Luke Skywalker's best friend in Episode IV?",
            listOf("Han Solo", "Biggs Darklighter", "Obi-Wan Kenobi", "Jed Porkins"), 2,
            100
        ), Question(
            "What is type of freighter is the Millennium Falcon?",
            listOf("VCX-100", "HWK-290", "G9 Rigger", "YT-1300"), 4, 250
        ), Question(
            "What planet did Kylo Ren find the 'resurrected' Emperor Palpatine on?",
            listOf("Dathomir", "Lothal", "Endor", "Exogol"), 4, 500
        ), Question(
            "What was the name of Anakin Skywalker's mother?",
            listOf("Shmi", "Ahsoka", "Leia", "Aayla"), 1, 1000
        ), Question(
            "What infamous legends character was introduced into the new canon in Rebels?",
            listOf("Starkiller", "Jacen Solo", "Thrawn", "Kyle Katarn"), 3,
            2000
        ), Question(
            "'Many __ died to bring us this information', What race of aliens died to provide" +
                    " the rebellion with the Death Star II plans?", listOf("Ithorians", "Amani",
                        "Trandoshans", "Bothans"), 4, 4500
        ), Question(
            "What bounty hunter from the prequels and The Clone Wars was revealed to be killed" +
            " by one of the protagonists from Solo?", listOf("Zuckuss", "Zam Wessell", "Jango Fett", "Aurra Sing")
            , 4, 9000
        )
    )

    //variables
    private lateinit var questionTextView: TextView
    private lateinit var answerGroup: RadioGroup
    private lateinit var confirmButton: Button
    private lateinit var earnings: TextView

    private var currQIndex = 0
    private var runningTotal = 0
    private var numCorrect = 0
    private var selectedRBtn: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        questionTextView = findViewById(R.id.questionTextView)
        answerGroup = findViewById(R.id.answerGroup)
        confirmButton = findViewById(R.id.confirmButton)
        earnings = findViewById(R.id.earnings)

        //display question
        displayQuestion(currQIndex)

        //sets background color for radio buttons upon selection
        answerGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedRBtn?.setBackgroundColor(Color.TRANSPARENT)
            selectedRBtn = findViewById(checkedId)
            selectedRBtn?.setBackgroundColor(Color.parseColor("#00FF00"))
        }
        
        confirmButton.setOnClickListener{
            //checks if radio button is selected
            val selected = answerGroup.checkedRadioButtonId

            //if an option is selected -> move on
            if (selected != -1) {
                //checks if selected radio button is correct option
                checkAnswer(selected)

                //checks if there is another question
                //if true increments question index and displays next question
                if (currQIndex < questions.size - 1) {
                    currQIndex++
                    displayQuestion(currQIndex)
                } else {
                    //when there are no more questions -> move to stat screen
                    val intent = Intent(this@QuestionActivity, StatsActivity::class.java)
                    startActivity(intent)

                    //saves total earnings and amount of correct answers
                    val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    val finalScore = runningTotal

                    editor.putInt("runningTotal", finalScore)
                    editor.putInt("finalCorrect", numCorrect)

                    editor.apply()

                    showToast("Game complete!")

                }
            } else {
                    showToast("Please select an option!")
                }
            }
        }

    //displays toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayQuestion(Index: Int) {
        //sets question based on provided index
        val question = questions[Index]
        //sets question text
        questionTextView.text = question.text

        //removes previous answer options
        answerGroup.removeAllViews()

        //sets new answer options
        for (i in question.options.indices) {
            val radioButton = RadioButton(this)
            radioButton.text = question.options[i]
            radioButton.id = i + 1
            answerGroup.addView(radioButton)
        }
        //unchecks previous selection
        answerGroup.clearCheck()
    }
    

    private fun checkAnswer(selected: Int) {
        val question = questions[currQIndex]

        //checks if selected radio button is equal to the correct option
        if (selected == question.correctOptionIndex) {
            //adds payout to total
            runningTotal+=question.payout
            //increments number of correct answers
            numCorrect++
            //displays total
            earnings.text = "You Earned: $runningTotal Imperial Credits"
            showToast("Correct! You earned ${question.payout} credits!")
        } else {
            showToast("Wrong answer, rebel scum!")
        }
    }
}
