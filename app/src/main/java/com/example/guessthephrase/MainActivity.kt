package com.example.guessthephrase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityLayout: ConstraintLayout
    lateinit var guessButton: Button
    lateinit var playAgainButton: Button
    lateinit var guessEditText: EditText
    lateinit var promptTextView: TextView
    lateinit var phraseTextView: TextView
    lateinit var guessedLettersTextView: TextView
    lateinit var messageArrayList: ArrayList<String>
    val phrase: String = "Hello World"
    var phraseDisplayed = ""
    var phraseGuess = 10
    var lettersGuess = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityLayout = findViewById(R.id.mainActivityLayout_cl)
        guessButton = findViewById(R.id.guess_button)
        playAgainButton = findViewById(R.id.playAgain_button)
        guessEditText = findViewById(R.id.guess_editText)
        promptTextView = findViewById(R.id.prompt_textview)
        phraseTextView = findViewById(R.id.phrase_textview)
        guessedLettersTextView = findViewById(R.id.remainingAttempts_textview)


        messageArrayList = ArrayList()
        phraseDisplayed = updateDisplayedPhrase("")

        phraseTextView.text = phraseDisplayed
        playAgainButton.isVisible = false
        guessEditText.hint = "Guess the full phrase"
        promptTextView.text = "Guess the full phrase"

        remainingAttempts_textview.text="Remaining Attempts: $phraseGuess"


        messages_recyclerView.adapter = MessagesRecyclerViewAdapter(messageArrayList)
        messages_recyclerView.layoutManager = LinearLayoutManager(this)

        guessButton.setOnClickListener {
            val userGuess = guessEditText.text.toString()
            if (userGuess.isNotEmpty() || userGuess.trim() != "") {
                if (phraseGuess > 0) {
                    if (userGuess.lowercase() == phrase.lowercase()) {
                        Snackbar.make(mainActivityLayout, "you won", Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        phraseGuess--
                        remainingAttempts_textview.text="Remaining Attempts: $phraseGuess"
                        messageArrayList.add("your guess is $userGuess \nwrong guess")
                        messages_recyclerView.adapter?.notifyItemInserted(messageArrayList.size)
                    }
                }else if(lettersGuess > 0 ){
                    guessEditText.hint = "Guess the a letter"
                    promptTextView.text = "Guess the a letter"
                    if(phrase.contains(userGuess,true)){
                        phraseDisplayed = updateDisplayedPhrase(userGuess)
                        phraseTextView.text = phraseDisplayed
                    }else{
                        lettersGuess--
                        remainingAttempts_textview.text="Remaining Attempts: $lettersGuess"
                        messageArrayList.add("your guess is $userGuess \nwrong guess")
                        messages_recyclerView.adapter?.notifyItemInserted(messageArrayList.size)
                    }
                }
            } else {
                Snackbar.make(mainActivityLayout, "Please enter your guess", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }


    }

    private fun updateDisplayedPhrase(userGuess: String): String {
        var updatedPhrase = phraseDisplayed
        for (character in phrase) {
            updatedPhrase += when {
                character.toString().equals(userGuess, true) -> character
                character == ' ' -> ' '
                else -> '*'
            }
        }
        return updatedPhrase
    }


}