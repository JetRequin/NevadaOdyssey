package fr.isen.nevadaodyssey

import android.annotation.SuppressLint
import android.content.Context
import com.squareup.seismic.ShakeDetector
import android.hardware.SensorManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dice.*
import java.util.*

/* =============================== LIAR'S DICE RULES ==========================
    Each player has five standard 6-sided dice
The game is played over multiple rounds.
The first player for the first round is determined by mutual agreement
or by all players rolling two dice with the highest roller becoming the first player.

To begin : Each player roll their dice, hidden from the other

The 1st player bid : a face and a quantity (ex: five "2's")
    the quantity representing the dice of all the players

The other can either then make a higher bid of the same face (ex:"six 2's"),
or they can challenge the previous bid.

If the player challenges the previous bid, all players reveal their dice.
    If the bid is matched or exceeded, the bidder wins.
    Otherwise the challenger wins.

 ==============================================================================
 */

class PokerDiceActivity: AppCompatActivity(),ShakeDetector.Listener{
    var amountBetValue = 0
    var faceBetValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)

// ==== SHAKE DETECTION ===========================================
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sd = ShakeDetector(this)
        sd.start(sensorManager)

        //Declare a DicePlayer() type
        val player = DicePlayer()
        val computer = DicePlayer()
        //hearShake()

        //Init turn with the correct amount of die to roll
        initTurn(player, computer)
        //playTurn(player, computer)

        buttonBet.setOnClickListener {
            playTurn(player,computer)
        }

        buttonLie.setOnClickListener{

        }
    }

    override fun hearShake() {
        Toast.makeText(applicationContext,"SHAAAKE",Toast.LENGTH_SHORT).show()
    }


    //Init party: roll the dice for both sides, choose the first better
    private fun initTurn(player: DicePlayer, computer: DicePlayer) {
        //Give each player his dice
        getDiceHand(player)
        getDiceHand(computer)

        printDiceImages(player, playerDice)
        amountBetValue = 0
        faceBetValue = 0

        //Choose who is going to bet first
        if ((1..player.maxDieValue).random() < (1..computer.maxDieValue).random()) {
            computer.isBetting = true

        } else {
            player.isBetting = true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun playTurn(player: DicePlayer, computer: DicePlayer) {
        // if numberOfDieToRoll = 0
        //      ==> END TURN and lose X money
/*
        if (player.isBetting) {
            textViewCurrentBet.text = "BET: " + "Face:"+ faceBetValue.toString() +"Number: "+ amountBetValue.toString()

            faceBetValue = editTextFaceNumber.text.toString().toInt()
            amountBetValue = editTextHighestBet.text.toString().toInt()
            //Turn change
            player.isBetting = false
            computer.isBetting = true
        }

        if (computer.isBetting) {
            textViewCurrentBet.text = "Computer is betting"

            // Have dice
            if (amountBetValue <= computer.numberOfDicePerValue.max()!!){
                amountBetValue = computer.numberOfDicePerValue.max()!! + 1

            }

            // Have another
            if (amountBetValue > computer.numberOfDicePerValue.max()!! + 2){
                amountBetValue = computer.numberOfDicePerValue.max()!!
            }
            else {
                turnLieResult(player, computer)
            }

            //Turn change
            player.isBetting = true
            computer.isBetting = false
        }
        // THE GOOD PLAYER HAVE TO PLACE A BET
        // TWO CHOICES : LIE! OR FOLLOW
        // COMPUTER = "Bet" if he play: Max possessed value +1
        //                     if player play AND if he has at least 1: Player value +1
        //                     if player play AND if he don't have 1 and bet : < 2 : OVER BET max possed value +1
        //            "Lie"    If he don't have any dice of bet and > 3
        //
        // HIGHEST BET VALUE +1

 */
    }

    private fun turnLieResult(player: DicePlayer, computer: DicePlayer) {

    }

    private fun printDiceImages(player: DicePlayer, layout: LinearLayout) {
        val diceImages = player
        for (i in 0 until diceImages.dice.size) {
            val rand = (0 until diceImages.dice.size).random()
            setImageDie(diceImages, layout)
            diceImages.dice.removeAt(rand)
        }
    }

    private fun setImageDie(player: DicePlayer, layout: LinearLayout) {
        val image = ImageView(this).apply {
            val id = context.resources.getIdentifier(
                getImageString(player),
                "drawable",
                context.packageName
            )
            setImageResource(id)

            adjustViewBounds = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.width = 200
            layoutParams.height = 300
        }
        layout.addView(image)
    }

    private fun getImageString(player: DicePlayer): String {
        return when (player.dice.last()) {
            1 -> {
                "die1"
            }
            2 -> {
                "die2"
            }
            3 -> {
                "die2"
            }
            4 -> {
                "die4"
            }
            5 -> {
                "die5"
            }
            6 -> {
                "die6"
            }
            else -> "error"
        }
    }
}
