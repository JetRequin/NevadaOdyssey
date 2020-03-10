package fr.isen.nevadaodyssey

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*

class HomeActivity : AppCompatActivity() {

    var userPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userPreferences = getSharedPreferences(UserPreferences.name, Context.MODE_PRIVATE)
        val name = userPreferences?.getString(UserPreferences.name,null)?: ""
        var money = userPreferences?.getInt(UserPreferences.money,0) ?: 0

        textViewUserName.text = name
        textViewUserMoney.text = "Money:"+ money.toString() +"$"

        //TODO:To each image button, link corresponding activity

             imageButtonBlackJack.setOnClickListener{
                 val intentBlackJack = Intent(this, BlackJackActivity::class.java)
                 intentBlackJack.putExtra("money", money)
                 startActivity(intentBlackJack)
             }

        /*
             imageButtonPokerDice.setOnClickListener {
                 val intentPokerDice = Intent(this, PokerDiceActivity::class.java)
                 startActivity(intentPokerDice)
             }*/

             imageButtonRoulette.setOnClickListener{
                 val intentRoulette = Intent(this, RouletteActivity::class.java)
                 startActivity(intentRoulette)
             }
/*
             imageButtonSlotMachine.setOnClickListener {
                 val intentSlotMachine = Intent(this, SlotMachineActivity::class.java)
                 startActivity(intentSlotMachine)
             }
              */
    }
}