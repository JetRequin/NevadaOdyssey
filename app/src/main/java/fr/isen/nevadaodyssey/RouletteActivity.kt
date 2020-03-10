package fr.isen.nevadaodyssey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Animation.*
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_roulette.*
import java.security.KeyStore
import java.util.*


class RouletteActivity : AppCompatActivity() {

    var userPreferences: SharedPreferences? = null
    var degree: Float = 0.0f
    var degree_old: Float = 0.0f
    lateinit var r: Random
    var player = Player()
    var theCurrentNum: String = ""

    //37 sectors, 9,72 deg each
    private var FACTOR : Float = 4.86f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roulette)
        r = Random()
        userPreferences = getSharedPreferences(UserPreferences.name, Context.MODE_PRIVATE)
        var money = userPreferences?.getInt(UserPreferences.money,0) ?: 0
        val mIntent = intent
        moneyAmount.text = "Money:"+ money.toString() +"$"

        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switch1.text = "Red"
            } else {
                switch1.text = "Black"

            }
        }


        spinButton.setOnClickListener {

            if(mise.text.toString().toInt() <= money) {
                //Toast.makeText(
                //this,
                // "c bon",
                // Toast.LENGTH_LONG).show()

                degree_old = degree % 360
                degree = r.nextInt(3600) + 720.0f
                val rotate = RotateAnimation(
                    degree_old,
                    degree,
                    RotateAnimation.RELATIVE_TO_SELF,
                    0.5f,
                    RotateAnimation.RELATIVE_TO_SELF,
                    0.5f
                )
                rotate.duration = 3600
                rotate.fillAfter = true
                rotate.interpolator = DecelerateInterpolator()
                rotate.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val currentNum = currentNumber(360 - (degree % 360))[0]
                        val myColor = currentNumber(360 - (degree % 360))[1]
                        result.text = currentNum

                        if(myColor == "red" && switch1.isChecked){
                            money = (money + (mise.text.toString().toInt()*1.5)).toInt()
                            moneyAmount.text = money.toString()                        }
                        else {
                            if (myColor == "black" && switch1.isChecked) {
                                money -= mise.text.toString().toInt()
                                moneyAmount.text = money.toString()
                            }
                            else{
                                if (myColor == "red") {
                                    money -= mise.text.toString().toInt()
                                    moneyAmount.text = money.toString()
                                }
                                else{
                                    money = (money + (mise.text.toString().toInt()*1.5)).toInt()
                                    moneyAmount.text = money.toString()
                                }
                            }
                        }



                    }

                    override fun onAnimationStart(animation: Animation?) {
                        result.text = ""
                    }
                })
                iv_wheel.startAnimation(rotate)
            }
            else{
                Toast.makeText(
                this,
                 "Not enough money",
                 Toast.LENGTH_LONG).show()
            }
        }




    }
    override fun onBackPressed() {
        super.onBackPressed()
        val editor = userPreferences?.edit()
        editor?.putInt(UserPreferences.money,player.money)
        editor?.apply()
        val intentHomeActivity = Intent(this, HomeActivity::class.java)
        startActivity(intentHomeActivity)
    }
    private fun currentNumber(degrees: Float): Array<String> {
        var text = ""
        var color = ""
        var number=""

        if(degrees >= (FACTOR*1) && degrees < (FACTOR*3)){
            text = "32 red"
            color = "red"
        }
        if(degrees >= (FACTOR*3) && degrees < (FACTOR*5)){
            text = "15 black"
            color = "black"

        }
        if(degrees >= (FACTOR*5) && degrees < (FACTOR*7)){
            text = "19 red"
            color = "red"

        }
        if(degrees >= (FACTOR*7) && degrees < (FACTOR*9)){
            text = "4 black"
            color = "black"

        }
        if(degrees >= (FACTOR*9) && degrees < (FACTOR*11)){
            text = "21 red"
            color = "red"

        }
        if(degrees >= (FACTOR*11) && degrees < (FACTOR*13)){
            text = "2 black"
            color = "black"

        }
        if(degrees >= (FACTOR*13) && degrees < (FACTOR*15)){
            text = "25 red"
            color = "red"

        }
        if(degrees >= (FACTOR*15) && degrees < (FACTOR*17)){
            text = "17 black"
            color = "black"

        }
        if(degrees >= (FACTOR*17) && degrees < (FACTOR*19)){
            text = "34 red"
            color = "red"

        }
        if(degrees >= (FACTOR*19) && degrees < (FACTOR*21)){
            text = "6 black"
            color = "black"

        }
        if(degrees >= (FACTOR*21) && degrees < (FACTOR*23)){
            text = "27 red"
            color = "red"

        }
        if(degrees >= (FACTOR*23) && degrees < (FACTOR*25)){
            text = "13 black"
            color = "black"

        }
        if(degrees >= (FACTOR*25) && degrees < (FACTOR*27)){
            text = "36 red"
            color = "red"

        }
        if(degrees >= (FACTOR*27) && degrees < (FACTOR*29)){
            text = "11 black"
            color = "black"

        }
        if(degrees >= (FACTOR*29) && degrees < (FACTOR*31)){
            text = "30 red"
            color = "red"

        }
        if(degrees >= (FACTOR*31) && degrees < (FACTOR*33)){
            text = "8 black"
            color = "black"

        }
        if(degrees >= (FACTOR*33) && degrees < (FACTOR*35)){
            text = "23 red"
            color = "red"

        }
        if(degrees >= (FACTOR*35) && degrees < (FACTOR*37)){
            text = "10 black"
            color = "black"

        }
        if(degrees >= (FACTOR*37) && degrees < (FACTOR*39)){
            text = "5 red"
            color = "red"

        }
        if(degrees >= (FACTOR*39) && degrees < (FACTOR*41)){
            text = "24 black"
            color = "black"

        }
        if(degrees >= (FACTOR*41) && degrees < (FACTOR*43)){
            text = "16 red"
            color = "red"

        }
        if(degrees >= (FACTOR*43) && degrees < (FACTOR*45)){
            text = "33 black"
            color = "black"

        }
        if(degrees >= (FACTOR*45) && degrees < (FACTOR*47)){
            text = "1 red"
            color = "red"

        }
        if(degrees >= (FACTOR*47) && degrees < (FACTOR*49)){
            text = "20 black"
            color = "black"

        }
        if(degrees >= (FACTOR*49) && degrees < (FACTOR*51)){
            text = "14 red"
            color = "red"

        }
        if(degrees >= (FACTOR*51) && degrees < (FACTOR*53)){
            text = "31 black"
            color = "black"

        }
        if(degrees >= (FACTOR*53) && degrees < (FACTOR*55)){
            text = "9 red"
            color = "red"

        }
        if(degrees >= (FACTOR*55) && degrees < (FACTOR*57)){
            text = "22 black"
            color = "black"

        }
        if(degrees >= (FACTOR*57) && degrees < (FACTOR*59)){
            text = "18 red"
            color = "red"

        }
        if(degrees >= (FACTOR*59) && degrees < (FACTOR*61)){
            text = "29 black"
            color = "black"

        }
        if(degrees >= (FACTOR*61) && degrees < (FACTOR*63)){
            text = "7 red"
            color = "red"

        }
        if(degrees >= (FACTOR*63) && degrees < (FACTOR*65)){
            text = "28 black"
            color = "black"

        }
        if(degrees >= (FACTOR*65) && degrees < (FACTOR*67)){
            text = "12 red"
            color = "red"

        }
        if(degrees >= (FACTOR*67) && degrees < (FACTOR*69)){
            text = "35 black"
            color = "black"

        }
        if(degrees >= (FACTOR*69) && degrees < (FACTOR*71)){
            text = "3 red"
            color = "red"

        }
        if(degrees >= (FACTOR*71) && degrees < (FACTOR*73)){
            text = "26 black"
            color = "black"

        }
        if((degrees >= (FACTOR*73) && degrees < 360) || (degrees >= 0 && degrees < (FACTOR*1))){
            text = "0 green"
            color = "green"
            number="0"

        }
        val tab = arrayOf(text, color)
        return tab
    }
}
