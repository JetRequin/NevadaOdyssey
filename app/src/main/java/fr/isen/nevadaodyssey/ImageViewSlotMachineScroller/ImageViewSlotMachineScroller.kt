package fr.isen.nevadaodyssey.ImageViewSlotMachineScroller
import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import fr.isen.nevadaodyssey.R
import fr.isen.nevadaodyssey.SlotMachineActivity
import kotlinx.android.synthetic.main.image_view_slot_machine_scroller.view.*


class ImageViewSlotMachineScroller:FrameLayout {


    internal lateinit var eventEnd:IEventEnd

    internal var last_result=0
    internal var oldValue = 0

    companion object {
        private val ANIMATION_DURATION=150
    }

    val value:Int
        get() = Integer.parseInt(nextImage.tag.toString())
    fun setEventEnd(eventEnd: SlotMachineActivity)
    {
        this.eventEnd = eventEnd
    }
    constructor(context: Context):super(context)
    {
        init(context)
    }
    constructor(context:Context,attrs:AttributeSet):super(context,attrs)
    {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.image_view_slot_machine_scroller, this)
        nextImage.translationY = height.toFloat()
    }

    fun setValueRandom(image:Int, num_rotate:Int)
    {
        currentImage.animate()
            .translationY((-height).toFloat())
            .setDuration(ANIMATION_DURATION.toLong()).start()

        nextImage.translationY = nextImage.height.toFloat()

        nextImage.animate().translationY(0f).setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    setImage(currentImage,oldValue%6)
                    currentImage.translationY=0f
                    if (oldValue != num_rotate)
                    {
                        setValueRandom(image,num_rotate)
                        oldValue++
                    }
                    else
                    {
                        last_result = 0
                        oldValue = 0
                        setImage(nextImage,image)
                        eventEnd.eventEnd(image%6, num_rotate)
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }

            }).start()

    }

    private fun setImage(img: ImageView?, value: Int) {
        when (value) {
            Util.BAR.number -> img?.setImageResource(R.drawable.bar_done)
            Util.LEMON.number -> img?.setImageResource(R.drawable.lemon_done)
            Util.ORANGE.number -> img?.setImageResource(R.drawable.orange_done)
            Util.SEVEN.number -> img?.setImageResource(R.drawable.sevent_done)
            Util.TRIPLE.number -> img?.setImageResource(R.drawable.triple_done)
            Util.WATERMELON.number -> img?.setImageResource(R.drawable.waternelon_done)
        }

        img?.tag = value
        last_result = value
    }
}