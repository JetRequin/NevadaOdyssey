package fr.isen.nevadaodyssey

import android.annotation.SuppressLint
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
                    Util.bar -> {
                        Common.SCORE += 250//3 barres
                        txt_score.text = Common.SCORE.toString()
                    }
                    Util.lemon -> {
                        Common.SCORE += 100//3 citrons
                        txt_score.text = Common.SCORE.toString()
                    }
                    Util.watermelon -> {
                        Common.SCORE += 150//3 pasteques
                        txt_score.text = Common.SCORE.toString()
                    }
                    Util.orange -> {
                        Common.SCORE += 200//3 oranges
                        txt_score.text = Common.SCORE.toString()
                    }
                    Util.seven -> {
                        Common.SCORE += 300//3 septs
                        txt_score.text = Common.SCORE.toString()
                    }
                    Util.triple -> {
                        Common.SCORE += 900//3 triples
                        txt_score.text = Common.SCORE.toString()
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
        setContentView(R.layout.activity_slotmachine)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        image.setEventEnd(this@SlotMachineActivity)
        image2.setEventEnd(this@SlotMachineActivity)
        image3.setEventEnd(this@SlotMachineActivity)

        up.setOnClickListener {
            if(Common.SCORE >= 50)
            {
                up.visibility = View.GONE
                down.visibility = View.VISIBLE
                image.setValueRandom(Random.nextInt(6),Random.nextInt(15-5+1)+5)//6 images
                image2.setValueRandom(Random.nextInt(6),Random.nextInt(15-5+1)+5)//6 images
                image3.setValueRandom(Random.nextInt(6),Random.nextInt(15-5+1)+5)//6 images

                Common.SCORE -= 50
                txt_score.text = Common.SCORE.toString()
            }
            else
            {
                Toast.makeText(this,"Pas Assez Riche",Toast.LENGTH_SHORT).show()
            }
        }
    }
}