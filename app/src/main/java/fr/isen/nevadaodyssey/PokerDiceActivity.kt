package fr.isen.nevadaodyssey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.squareup.seismic.ShakeDetector
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
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
 ============================================================================== */

class PokerDiceActivity: AppCompatActivity(),ShakeDetector.Listener{
    var money = 0
    private var userPreferences: SharedPreferences? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)

        userPreferences = getSharedPreferences(UserPreferences.name, Context.MODE_PRIVATE)
        money = userPreferences?.getInt(UserPreferences.money,0) ?: 0
        textViewUserMoney.text = "Money:$money$"

// ==== SHAKE DETECTION ===========================================
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sd = ShakeDetector(this)
        sd.start(sensorManager)

        //Declare a DicePlayer() type
        val player = DicePlayer()
        val computer = DicePlayer()
        val bet = DiceBet()
        bet.faceBetValue = 0
        bet.amountBetValue = 0

        //Init turn with the correct amount of die to roll and first turn
        initTurn(player, computer)
        printDiceImages(player, playerDiceLayout)
        //First turn
        playTurn(player,computer,bet)

// Button listener ===============================================
        buttonBet.setOnClickListener {
            playTurn(player,computer,bet)
        }

        buttonLie.setOnClickListener{
            turnLieResult(player,computer,bet) //Will take a dice from looser
            checkGameOver(player,computer) // Will check if party is over
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val editor = userPreferences?.edit()
        editor?.putInt(UserPreferences.money,money)
        editor?.apply()
        val intentHomeActivity = Intent(this, HomeActivity::class.java)
        startActivity(intentHomeActivity)
    }

    override fun hearShake() {
        val player = DicePlayer()
        val computer = DicePlayer()
        initTurn(player, computer)

        Toast.makeText(applicationContext,"SHAKE",Toast.LENGTH_SHORT).show()
    }


    //Init party: roll the dice for both sides, choose the first better
    private fun initTurn(player: DicePlayer, computer: DicePlayer) {
        textViewCurrentBet.text = getString(R.string.place_bet)
        //Give each player his dice
        getDiceHand(player)
        getDiceHand(computer)

        printDiceImages(player, playerDiceLayout)

        //Choose who is going to bet first
        if ((1..player.maxDieValue).random() < (1..computer.maxDieValue).random()) {
            computer.isBetting = true

        } else {
            player.isBetting = true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun playTurn(player: DicePlayer, computer: DicePlayer, bet:DiceBet) {
// ====================== PLAYER BET ===========================================================
        if (player.isBetting) {
            // Read bet
            bet.faceBetValue = editTextFaceNumber.text.toString().toIntOrNull()!!
            bet.amountBetValue = editTextHighestBet.text.toString().toIntOrNull()!!

            textViewCurrentBet.text = "Your bet:\n" + "Face: "+ bet.faceBetValue.toString() +"\nNumber: "+ bet.amountBetValue.toString()
            //Turn change
            player.isBetting = false
            computer.isBetting = true
        }
// ====================== COMPUTER BET ========================================================
        if (computer.isBetting) {
            //Extremely simple mode
            textViewCurrentBet.text = "Computer bet:\n" + " Face: "+ bet.faceBetValue.toString() +"Number: "+ bet.amountBetValue.toString()
            if (bet.amountBetValue == 0){
                bet.amountBetValue = 3
                bet.faceBetValue = 3
            }
            if (bet.amountBetValue <= computer.numberOfDicePerValue[bet.faceBetValue]){
                    bet.amountBetValue = computer.numberOfDicePerValue[bet.faceBetValue] + 1
            }
                else {
                    turnLieResult(computer,player,bet)
                }
            //Turn change
            player.isBetting = true
            computer.isBetting = false
        }
    }

    private fun turnLieResult(player: DicePlayer, liar:DicePlayer, bet: DiceBet) {
        if (bet.amountBetValue > 0 && bet.amountBetValue < (player.numberOfDieToRoll + liar.numberOfDieToRoll) && bet.faceBetValue<= 6 ){
            if(bet.amountBetValue <=  liar.numberOfDicePerValue[bet.faceBetValue] ){
                // Not a lie
                textViewCurrentBet.text = getString(R.string.truth)
                player.numberOfDieToRoll = player.numberOfDieToRoll - 1

            }
            else{
                textViewCurrentBet.text = getString(R.string.lie)
                liar.numberOfDieToRoll = liar.numberOfDieToRoll - 1
            }
        }
        else {
            textViewCurrentBet.text = getString(R.string.incorrect_bet)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkGameOver(player: DicePlayer, computer: DicePlayer){
        if(player.numberOfDieToRoll == 0) {
            textViewCurrentBet.text = "You win!\nShake your phone to play again"
            money=+1000
        }
        if(computer.numberOfDieToRoll == 0) {
            textViewCurrentBet.text = "Game over\n Shake your phone to play again"
            money=-1000
        }
    }

    // ========================== FUNCTIONS TO PRINT IMAGES ======================================
    //Print dice images corresponding to values
    private fun printDiceImages(player: DicePlayer, layout: LinearLayout) {
        layout.removeAllViewsInLayout()

        for(i in 0 until player.dice.size) {
            val image = ImageView(this).apply {
                val id = context.resources.getIdentifier(
                    getImageString(player, i),
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
    }

    //Load images names
    private fun getImageString(player : DicePlayer,i :Int): String {
        return when (player.dice[i]) {
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
