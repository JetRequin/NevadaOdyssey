package fr.isen.nevadaodyssey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var userPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userPreferences = getSharedPreferences(UserPreferences.name, Context.MODE_PRIVATE)
        val name = userPreferences?.getString(UserPreferences.name,null)?: ""
        val money = userPreferences?.getInt(UserPreferences.money,0) ?: 0

        textViewUserName.text = name
        textViewUserMoney.text = "Money:"+ money.toString() +"$"

             imageButtonBlackJack.setOnClickListener{
                 val intentBlackJack = Intent(this, BlackJackActivity::class.java)
                 intentBlackJack.putExtra("money", money)
                 startActivity(intentBlackJack)
             }

             imageButtonPokerDice.setOnClickListener {
                 val intentPokerDice = Intent(this, PokerDiceActivity::class.java)
                 intentPokerDice.putExtra("money", money)
                 startActivity(intentPokerDice)
             }

             imageButtonSlotMachine.setOnClickListener {
                 val intentSlotMachine = Intent(this, SlotMachineActivity::class.java)
                 intentSlotMachine.putExtra("money", money)
                 startActivity(intentSlotMachine)
             }

            imageButtonRoulette.setOnClickListener{
                val intentRoulette = Intent(this, RouletteActivity::class.java)
                intentRoulette.putExtra("money", money)
                startActivity(intentRoulette)
            }
    }
}