package com.example.guessthephrase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
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

    var phrase = "Hello World"
    val phraseDictionary = mutableMapOf<Int, Char>()
    private var userGuess = ""
    private var userLetterGuess = ""
    private var count = 0
    var phraseStage = true


    lateinit var messageArrayList: ArrayList<String>

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

        messages_recyclerView.adapter = MessagesRecyclerViewAdapter(messageArrayList)
        messages_recyclerView.layoutManager = LinearLayoutManager(this)

        playAgainButton.isVisible = false
        guessedLettersTextView.isVisible = false

        guessButton.setOnClickListener { addMessage() }

        for (i in phrase.indices) {
            if (phrase[i] == ' ') {
                phraseDictionary[i] = ' '
                userGuess += ' '
            } else {
                phraseDictionary[i] = '*'
                userGuess += '*'
            }
        }
        updateText()


    }

    private fun addMessage() {
        var guess = guessEditText.text.toString().lowercase()

        if (phraseStage) {
            if (guess == phrase.lowercase()) {
                showDialog("You won!", "you guessed correctly!")
                guessEditText.isVisible = false
                guessButton.isVisible = false
                playAgainButton.isVisible = true
            } else {
                messageArrayList.add("Wrong guess: $guess")
                phraseStage = false
                updateText()
            }
        } else {
            if (guess.isNotEmpty()) {
                userGuess = ""
                checkLetters(guess)
            } else {
                Snackbar.make(
                    mainActivityLayout,
                    "Please enter one letter at least",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        guessEditText.text.clear()
        guessEditText.clearFocus()
        messages_recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun checkLetters(guess: String) {
        val guessedLetter = guess.first()
        var found = 0
        for (i in phrase.indices) {
            if (phrase[i] == guessedLetter) {
                phraseDictionary[i] = guessedLetter
                found++
            }
        }
        for (i in phraseDictionary) {
            userGuess += phraseDictionary[i.key]
        }

        if (userGuess.lowercase() == phrase.lowercase()) {
            showDialog("You won!", "you guessed correctly!")
            guessEditText.isVisible = false
            guessButton.isVisible = false
            playAgainButton.isVisible = true
        }
            if (userLetterGuess.isEmpty()) {
                userLetterGuess += guessedLetter
            } else {
                "$userLetterGuess,  $guessedLetter"
            }
            if (found > 0) {
                messageArrayList.add("Found $found ${guessedLetter.toUpperCase()}(s)")
            } else {
                messageArrayList.add("No ${guessedLetter.toUpperCase()}s found")
            }
            count++
            val guessesLeft = 10 - count
            if (count < 10) {
                messageArrayList.add("$guessesLeft guesses remaining")
            }

        updateText()
        messages_recyclerView.scrollToPosition(messageArrayList.size - 1)
    }

    private fun updateText() {
        phraseTextView.text = "Phrase:  $userGuess"
        guessedLettersTextView.text = "Guessed Letters:  $userLetterGuess"
        if (phraseStage) {
            guessEditText.hint = "Guess the full phrase"
            promptTextView.text = "Guess the full phrase"

        } else {
            guessedLettersTextView.isVisible = true
            promptTextView.text = "Guess a letter"
            guessEditText.hint = "Guess a letter"
        }
    }


    private fun showDialog(title: String, message: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("$message\nDo you want to play again?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                this.recreate()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }

}