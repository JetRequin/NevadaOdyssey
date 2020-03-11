package fr.isen.nevadaodyssey
import android.util.Log
//dice:plural / die:singular
//Just in case

class DicePlayer {
    var numberOfDieToRoll = 5
    val maxDieValue = 6

    var isBetting = false
    var dice = ArrayList<Int>(numberOfDieToRoll)
    var numberOfDicePerValue = Array(maxDieValue){0}
}

// Roll a single die
fun rollDie(player: DicePlayer) {
    val dieValue = (1..player.maxDieValue).random()
    player.dice.add(dieValue)
    player.numberOfDicePerValue[dieValue-1]++
}

// Give all the dice possible for the current numberOfDieToRoll
fun getDiceHand(player: DicePlayer){
    for(i in 1..player.numberOfDieToRoll )
    {
        rollDie(player)
    }

    Log.d("Dices","Dice: " + player.dice)
    for(i in 1..player.maxDieValue)
    {
        Log.d("Dices","Number of "+ i +":" + player.numberOfDicePerValue[i-1])
    }
}