package fr.isen.nevadaodyssey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_roulette.*
import java.util.*


class RouletteActivity : AppCompatActivity() {

    private var userPreferences: SharedPreferences? = null
    var degree: Float = 0.0f
    private var degreeOld: Float = 0.0f
    private lateinit var r: Random
    var money=0

    //37 sectors, 9,72 deg each
    private var factor : Float = 4.86f

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roulette)
        r = Random()
        userPreferences = getSharedPreferences(UserPreferences.name, Context.MODE_PRIVATE)
        money = userPreferences?.getInt(UserPreferences.money,0) ?: 0
        moneyAmount.text = "Money:$money$"

        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch1.text = getString(R.string.red_bet)
            } else {
                switch1.text = getString(R.string.black_bet)
            }
        }

        //Spin roulette
        spinButton.setOnClickListener {
            if(wheel_bet.text.toString().toInt() <= money) {
                //Toast.makeText(
                //this,
                // "c bon",
                // Toast.LENGTH_LONG).show()

                degreeOld = degree % 360
                degree = r.nextInt(3600) + 720.0f
                val rotate = RotateAnimation(
                    degreeOld,
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
                        val myNumber = currentNumber(360 - (degree % 360))[2]
                        result.text = currentNum
                        if(numberBet.text.isNotEmpty()){
                            if(numberBet.text.toString() == myNumber){
                                money = (money + (wheel_bet.text.toString().toInt() * 10))
                                moneyAmount.text = money.toString()
                            }
                            else{
                                money -= wheel_bet.text.toString().toInt()
                                moneyAmount.text = money.toString()
                            }
                        }
                        else {
                            if (myColor == "red" && switch1.isChecked) {
                                money = (money + (wheel_bet.text.toString().toInt() * 1.5)).toInt()
                                moneyAmount.text = money.toString()
                            } else {
                                if (myColor == "black" && switch1.isChecked) {
                                    money -= wheel_bet.text.toString().toInt()
                                    moneyAmount.text = money.toString()
                                } else {
                                    if (myColor == "red") {
                                        money -= wheel_bet.text.toString().toInt()
                                        moneyAmount.text = money.toString()
                                    } else {
                                        money =
                                            (money + (wheel_bet.text.toString().toInt() * 1.5)).toInt()
                                        moneyAmount.text = money.toString()
                                    }
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
        editor?.putInt(UserPreferences.money,money)
        editor?.apply()
        val intentHomeActivity = Intent(this, HomeActivity::class.java)
        startActivity(intentHomeActivity)
    }
    private fun currentNumber(degrees: Float): Array<String> {
        var text = ""
        var color = ""
        var number=""

        if(degrees >= (factor*1) && degrees < (factor*3)){
            text = "32 red"
            color = "red"
            number = "32"
        }
        if(degrees >= (factor*3) && degrees < (factor*5)){
            text = "15 black"
            color = "black"
            number = "15"

        }
        if(degrees >= (factor*5) && degrees < (factor*7)){
            text = "19 red"
            color = "red"
            number = "19"

        }
        if(degrees >= (factor*7) && degrees < (factor*9)){
            text = "4 black"
            color = "black"
            number = "4"

        }
        if(degrees >= (factor*9) && degrees < (factor*11)){
            text = "21 red"
            color = "red"
            number = "21"

        }
        if(degrees >= (factor*11) && degrees < (factor*13)){
            text = "2 black"
            color = "black"
            number = "2"

        }
        if(degrees >= (factor*13) && degrees < (factor*15)){
            text = "25 red"
            color = "red"
            number = "25"

        }
        if(degrees >= (factor*15) && degrees < (factor*17)){
            text = "17 black"
            color = "black"
            number = "17"

        }
        if(degrees >= (factor*17) && degrees < (factor*19)){
            text = "34 red"
            color = "red"
            number = "34"

        }
        if(degrees >= (factor*19) && degrees < (factor*21)){
            text = "6 black"
            color = "black"
            number = "6"

        }
        if(degrees >= (factor*21) && degrees < (factor*23)){
            text = "27 red"
            color = "red"
            number = "27"

        }
        if(degrees >= (factor*23) && degrees < (factor*25)){
            text = "13 black"
            color = "black"
            number = "13"

        }
        if(degrees >= (factor*25) && degrees < (factor*27)){
            text = "36 red"
            color = "red"
            number = "36"

        }
        if(degrees >= (factor*27) && degrees < (factor*29)){
            text = "11 black"
            color = "black"
            number = "11"

        }
        if(degrees >= (factor*29) && degrees < (factor*31)){
            text = "30 red"
            color = "red"
            number = "30"

        }
        if(degrees >= (factor*31) && degrees < (factor*33)){
            text = "8 black"
            color = "black"
            number = "8"

        }
        if(degrees >= (factor*33) && degrees < (factor*35)){
            text = "23 red"
            color = "red"
            number = "23"

        }
        if(degrees >= (factor*35) && degrees < (factor*37)){
            text = "10 black"
            color = "black"
            number = "10"

        }
        if(degrees >= (factor*37) && degrees < (factor*39)){
            text = "5 red"
            color = "red"
            number = "5"

        }
        if(degrees >= (factor*39) && degrees < (factor*41)){
            text = "24 black"
            color = "black"
            number = "24"

        }
        if(degrees >= (factor*41) && degrees < (factor*43)){
            text = "16 red"
            color = "red"
            number = "16"

        }
        if(degrees >= (factor*43) && degrees < (factor*45)){
            text = "33 black"
            color = "black"
            number = "33"

        }
        if(degrees >= (factor*45) && degrees < (factor*47)){
            text = "1 red"
            color = "red"
            number = "1"

        }
        if(degrees >= (factor*47) && degrees < (factor*49)){
            text = "20 black"
            color = "black"
            number = "20"

        }
        if(degrees >= (factor*49) && degrees < (factor*51)){
            text = "14 red"
            color = "red"
            number = "14"

        }
        if(degrees >= (factor*51) && degrees < (factor*53)){
            text = "31 black"
            color = "black"
            number = "31"

        }
        if(degrees >= (factor*53) && degrees < (factor*55)){
            text = "9 red"
            color = "red"
            number = "9"

        }
        if(degrees >= (factor*55) && degrees < (factor*57)){
            text = "22 black"
            color = "black"
            number = "22"

        }
        if(degrees >= (factor*57) && degrees < (factor*59)){
            text = "18 red"
            color = "red"
            number = "18"

        }
        if(degrees >= (factor*59) && degrees < (factor*61)){
            text = "29 black"
            color = "black"
            number = "29"

        }
        if(degrees >= (factor*61) && degrees < (factor*63)){
            text = "7 red"
            color = "red"
            number = "7"

        }
        if(degrees >= (factor*63) && degrees < (factor*65)){
            text = "28 black"
            color = "black"
            number = "28"

        }
        if(degrees >= (factor*65) && degrees < (factor*67)){
            text = "12 red"
            color = "red"
            number = "12"

        }
        if(degrees >= (factor*67) && degrees < (factor*69)){
            text = "35 black"
            color = "black"
            number = "35"

        }
        if(degrees >= (factor*69) && degrees < (factor*71)){
            text = "3 red"
            color = "red"
            number = "3"

        }
        if(degrees >= (factor*71) && degrees < (factor*73)){
            text = "26 black"
            color = "black"
            number = "26"

        }
        if((degrees >= (factor*73) && degrees < 360) || (degrees >= 0 && degrees < (factor*1))){
            text = "0 green"
            color = "green"
            number="0"

        }
        return arrayOf(text, color, number)
    }
}
