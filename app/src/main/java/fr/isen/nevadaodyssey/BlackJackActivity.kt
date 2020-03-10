package fr.isen.nevadaodyssey

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_black_jack.*
import java.util.*


class BlackJackActivity : AppCompatActivity() {
    var userPreferences: SharedPreferences? = null
    val player = Player()
    val dealer = Player()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)
        var deck : ArrayList<Card> = createDeck()
        var bet = 0
        var resetParty = false
        var resetDouble = false
        userPreferences = getSharedPreferences(UserPreferences.name, Context.MODE_PRIVATE)
        var money = userPreferences?.getInt(UserPreferences.money,0) ?: 0
        for(i in 0 until deck.size)
        {
            Log.d("Deck","Your card is "+deck[i].value +" of " +deck[i].type)
        }
        Log.d("Deck", "This is the size of the deck :"+deck.size)
        player.initMoney(money)
        moneyShow.text=player.money.toString()+"$"
        Log.d("InitParty", "You have "+ player.money+" dollars")
        initParty(deck,player, dealer,playerSet,dealerSet)
        showConsoleCards(player, dealer, deck)
        playerCard.text=printCard(player)
        hitButton.setOnClickListener {
            if(!resetParty && bet!=0)
            {
                drawCard(deck = deck,player = player,linearLayout = playerSet)
                Log.d("InitParty", "You have "+ player.getTotalPoints()+" points")
                playerCard.text=printCard(player)
                if(burst(player))
                {
                    endPartyLoosePlayer(player,bet)
                    winnerText.text="You loose"
                    Log.d("InitParty", "You have "+ player.getTotalPoints()+" points")
                    Log.d("InitParty", "Dealer has "+ dealer.getTotalPoints()+" points")
                    removeCards(dealerSet)
                    showAllCardsEvenHide(dealer,dealerSet)
                    resetParty=true
                }
                Log.d("InitParty", "This is the new size of the deck :"+deck.size)
                resetDouble=true
                betBar.visibility = View.GONE
            }
        }
        standButton.setOnClickListener {
            if(!resetParty && bet!=0)
            {
                while(dealer.getTotalPoints()<17 && !burst(dealer) || player.getTotalPoints()>dealer.getTotalPoints())
                {
                    drawCard(deck=deck,player = dealer,linearLayout =dealerSet)
                }
                Log.d("InitParty", "Dealer has "+ dealer.getTotalPoints()+" points")
                if(burst(dealer))
                {
                    endPartyWinPlayer(player,bet)
                    winnerText.text="You win"
                    removeCards(dealerSet)
                    showAllCardsEvenHide(dealer,dealerSet)
                }
                else
                {
                    winnerText.text=endPartyWithoutBurst(player,dealer,bet)
                    removeCards(dealerSet)
                    showAllCardsEvenHide(dealer,dealerSet)

                }
                resetParty=true
                resetDouble=true
                betBar.visibility = View.GONE
            }
        }
        resetButton.setOnClickListener {
            if(resetParty)
            {
                deck=resetParty(deck, player, dealer)
                removeCards(playerSet)
                removeCards(dealerSet)
                playerCard.text=printCard(player)
                CardValue.AS.number=11
                initParty(deck,player, dealer,playerSet,dealerSet)
                playerCard.text=printCard(player)
                showConsoleCards(player, dealer, deck)
                winnerText.text=""
                resetParty=false
                resetDouble=false
                moneyShow.text=player.money.toString()+"$"
                betBar.visibility = View.VISIBLE
            }
        }
        doubleButton.setOnClickListener {
            if(!resetParty && !resetDouble && bet!=0 && 2*bet<=player.money)
            {
                drawCard(deck=deck,player = player,linearLayout = playerSet)
                playerCard.text=printCard(player)
                if(burst(player))
                {
                    endPartyLoosePlayer(player,2*bet)
                    winnerText.text="You loose"
                    removeCards(dealerSet)
                    showAllCardsEvenHide(dealer,dealerSet)
                }
                else
                {
                    while(dealer.getTotalPoints()<17 && !burst(dealer) || player.getTotalPoints()>dealer.getTotalPoints())
                    {
                        drawCard(deck=deck,player = dealer,linearLayout =dealerSet)
                    }
                    Log.d("InitParty", "Dealer has "+ dealer.getTotalPoints()+" points")
                    if(burst(dealer))
                    {
                        endPartyWinPlayer(player,2*bet)
                        winnerText.text="You win"
                        removeCards(dealerSet)
                        showAllCardsEvenHide(dealer,dealerSet)
                    }
                    else
                    {
                        winnerText.text=endPartyWithoutBurst(player,dealer,2*0)
                        removeCards(dealerSet)
                        showAllCardsEvenHide(dealer,dealerSet)

                    }
                }
                resetParty=true
                resetDouble=true
                betBar.visibility = View.GONE

            }
            else if(2*bet>player.money)
            {
                Toast.makeText(applicationContext,"You can't bet more that you have in your wallet",Toast.LENGTH_SHORT).show()
            }
        }
       betBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                bet=(i*player.money)/100
                moneyShow.text=(player.money-bet).toString()+"$"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val editor = userPreferences?.edit()
        editor?.putInt(UserPreferences.money,player.money)
        editor?.apply()
        val intentHomeActivity = Intent(this, HomeActivity::class.java)
        startActivity(intentHomeActivity)
    }

    override fun onPause() {
        super.onPause()
        val editor = userPreferences?.edit()
        editor?.putInt(UserPreferences.money,player.money)
        editor?.apply()
        //val intentHomeActivity = Intent(this, HomeActivity::class.java)
        //startActivity(intentHomeActivity)

    }
    private fun initParty(deck: ArrayList<Card>, player:Player, dealer:Player, playerLayout: LinearLayout, dealerLayout: LinearLayout)
    {
        drawCard(deck = deck,player = player,linearLayout = playerLayout)
        drawHideCard(deck = deck, dealer = dealer, dealerLayout = dealerLayout)
        drawCard(deck = deck,player = player, linearLayout = playerLayout)
        drawCard(deck = deck,player = dealer, linearLayout = dealerLayout)
    }
    private fun drawCard(deck: ArrayList<Card>, player:Player, linearLayout: LinearLayout)
    {
        val rand = (0 until deck.size).random()
        //Log.d("Party", "Random value :$rand")
        player.addCard(deck[rand])
        showCard(player.cards.last(),linearLayout)
        deck.removeAt(rand)
    }
    private fun drawHideCard(deck: ArrayList<Card>, dealer: Player, dealerLayout: LinearLayout)
    {
        val rand = (0 until deck.size).random()
        dealer.addCard(deck[rand])
        val image = ImageView(this).apply {
            val id = context.resources.getIdentifier("gray_back", "drawable", context.packageName)
            //Log.d("Party", "Id value is $id")
            setImageResource(id)
            // set the ImageView bounds to match the Drawable's dimensions
            adjustViewBounds = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.width=300
            layoutParams.height=300
        }
        deck.removeAt(rand)
        // Add the ImageView to the layout.
        dealerLayout.addView(image)
    }

    private fun showAllCardsEvenHide(dealer: Player, dealerLayout: LinearLayout)
    {
        for(i in 0 until dealer.cards.size)
        {
            val image = ImageView(this).apply {
                val id = context.resources.getIdentifier(associateCard(dealer.cards[i]), "drawable", context.packageName)
                //Log.d("Party", "Id value is $id")
                setImageResource(id)

                // set the ImageView bounds to match the Drawable's dimensions
                adjustViewBounds = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.width=300
                layoutParams.height=300
            }
            // Add the ImageView to the layout.
            dealerLayout.addView(image)
        }
    }

    private fun endPartyWinPlayer(player: Player, bet: Int)
    {
        player.addRemoveMoney(bet)
        Log.d("Party", "Player win")
    }
    private fun endPartyLoosePlayer(player: Player, bet: Int)
    {
        player.addRemoveMoney(-bet)
        Log.d("Party", "Player loose")
    }

    private fun endPartyWithoutBurst(player: Player, dealer: Player, bet: Int) : String
    {
        return when {
            player.getTotalPoints()>dealer.getTotalPoints() -> {
                endPartyWinPlayer(player,bet)
                "You win"
            }
            player.getTotalPoints()<dealer.getTotalPoints() -> {
                endPartyLoosePlayer(player,bet)
                "You loose"
            }
            else -> {
                player.addRemoveMoney(0)
                Log.d("Party", "it's a draw")
                "It's a draw"
            }
        }
    }

    private fun printCard(player: Player): String {
        return player.getTotalPoints().toString()+" points"
    }
    private fun resetParty(deck: ArrayList<Card>, player: Player, dealer: Player) : ArrayList<Card>
    {
        val deckNew: ArrayList<Card>
        if(deck.size<10)
        {
            deckNew= createDeck()
            deck.clear()
            player.removeCards()
            dealer.removeCards()
        }
        else
        {
            deckNew=deck
            player.removeCards()
            dealer.removeCards()
        }
        return deckNew
    }

    private fun burst(player: Player) : Boolean
    {
        return player.getTotalPoints()>21
    }
/*
    fun delay(callBack:()-> Unit)
    {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                callBack.invoke()
            }
        }, 2000)
    }

 */
    private fun showConsoleCards(player: Player, dealer: Player, deck: ArrayList<Card>)
    {
        for(i in player.cards.indices)
        {
            Log.d("InitParty","Player has "+player.cards[i].value +" of " +player.cards[i].type)
        }
        for(i in dealer.cards.indices)
        {
            Log.d("InitParty","Dealer has "+dealer.cards[i].value +" of " +dealer.cards[i].type)
        }
        Log.d("InitParty", "This is the new size of the deck :"+deck.size)
        Log.d("InitParty", "You have "+ player.getTotalPoints()+" points")
        Log.d("InitParty", "Dealer has "+ dealer.getTotalPoints()+" points")

    }
    private fun showCard(card: Card, linearLayout: LinearLayout)
    {
        val image = ImageView(this).apply {
            val id = context.resources.getIdentifier(associateCard(card), "drawable", context.packageName)
            //Log.d("Party", "Id value is $id")
            setImageResource(id)

            // set the ImageView bounds to match the Drawable's dimensions
            adjustViewBounds = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.width=300
            layoutParams.height=300
        }
        // Add the ImageView to the layout.
       linearLayout.addView(image)
    }

    private fun removeCards(linearLayout: LinearLayout)
    {
        linearLayout.removeAllViewsInLayout()
    }

    private fun associateCard(card: Card): String
    {
        var stringCard : String = when (card.type) {
            CardType.DIAMONDS -> {
                "d_"
            }
            CardType.CLUBS -> {
                "c_"
            }
            CardType.HEARTS -> {
                "h_"
            }
            CardType.SPADES -> {
                "s_"
            }
        }

        when (card.value) {
            CardValue.AS -> {
                stringCard += "a"
            }
            CardValue.TWO -> {
                stringCard += "2"
            }
            CardValue.THREE -> {
                stringCard += "3"
            }
            CardValue.FOUR -> {
                stringCard += "4"
            }
            CardValue.FIVE -> {
                stringCard += "5"
            }
            CardValue.SIX -> {
                stringCard += "6"
            }
            CardValue.SEVEN -> {
                stringCard += "7"
            }
            CardValue.EIGHT -> {
                stringCard += "8"
            }
            CardValue.NINE -> {
                stringCard += "9"
            }
            CardValue.TEN -> {
                stringCard += "10"
            }
            CardValue.JACK -> {
                stringCard += "j"
            }
            CardValue.QUEEN -> {
                stringCard += "q"
            }
            CardValue.KING -> {
                stringCard += "k"
            }
        }
        //Log.d("Party", "String value is $stringCard")
        return stringCard
    }
}

