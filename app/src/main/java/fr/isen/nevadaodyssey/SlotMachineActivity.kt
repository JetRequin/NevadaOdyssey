package fr.isen.nevadaodyssey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import fr.isen.nevadaodyssey.ImageViewSlotMachineScroller.IEventEnd
import fr.isen.nevadaodyssey.ImageViewSlotMachineScroller.Util
import kotlinx.android.synthetic.main.activity_slotmachine.*
import kotlin.random.Random

class SlotMachineActivity : AppCompatActivity(), IEventEnd {
    var userPreferences: SharedPreferences? = null
    private var money =0
    override fun eventEnd(result: Int, count: Int){
        if(countDown < 2)
            countDown++
        else
        {
            down.visibility = View.GONE
            up.visibility = View.VISIBLE

            countDown = 0

            if(image.value == image2.value && image2.value == image3.value)
            {
                Toast.makeText(this,"Win",Toast.LENGTH_SHORT).show()
                when (image.value) {
                    Util.BAR.number -> {
                        money += Util.BAR.score//3 barres
                        txt_score.text = money.toString()
                    }
                    Util.LEMON.number -> {
                        money += Util.LEMON.score//3 citrons
                        txt_score.text = money.toString()
                    }
                    Util.WATERMELON.number -> {
                        money += Util.WATERMELON.score//3 pasteques
                        txt_score.text = money.toString()
                    }
                    Util.ORANGE.number -> {
                        money += Util.ORANGE.score//3 oranges
                        txt_score.text = money.toString()
                    }
                    Util.SEVEN.number -> {
                        money +=Util.SEVEN.score//3 septs
                        txt_score.text = money.toString()
                    }
                    Util.TRIPLE.number -> {
                        money += Util.TRIPLE.score//3 triples
                        txt_score.text = money.toString()
                    }
                }
            }
            else
            {
                Toast.makeText(this,"Lost",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var countDown=0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferences = getSharedPreferences(UserPreferences.name, Context.MODE_PRIVATE)
        money = userPreferences?.getInt(UserPreferences.money,0) ?: 0
        setContentView(R.layout.activity_slotmachine)
        txt_score.text=money.toString()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        image.setEventEnd(this@SlotMachineActivity)
        image2.setEventEnd(this@SlotMachineActivity)
        image3.setEventEnd(this@SlotMachineActivity)

        up.setOnClickListener {
            if(money>= 50)
            {
                up.visibility = View.GONE
                down.visibility = View.VISIBLE
                image.setValueRandom(Random.nextInt(6),Random.nextInt(15-5+1)+5)//6 images
                image2.setValueRandom(Random.nextInt(6),Random.nextInt(15-5+1)+5)//6 images
                image3.setValueRandom(Random.nextInt(6),Random.nextInt(15-5+1)+5)//6 images

                money -= 50
                txt_score.text = money.toString()
            }
            else
            {
                Toast.makeText(this,"Pas Assez Riche",Toast.LENGTH_SHORT).show()
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

    override fun onPause() {
        super.onPause()
        val editor = userPreferences?.edit()
        editor?.putInt(UserPreferences.money,money)
        editor?.apply()
        //val intentHomeActivity = Intent(this, HomeActivity::class.java)
        //startActivity(intentHomeActivity)

    }
}